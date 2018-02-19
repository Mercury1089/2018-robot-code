package org.usfirst.frc.team1089.robot.sensors;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.util.BoundingBox;
import org.usfirst.frc.team1089.util.MercMath;
import org.usfirst.frc.team1089.util.config.SensorsSettings;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * PixyCam implementation using SPI interface
 *
 * @deprecated since we cannot use the SPI interface on the RIO.
 */
public class PixySPI implements PIDSource {
    private static final Logger log = LogManager.getLogger(PixySPI.class);

    private final SPI SPI;

    // Variables used for SPI comms, derived from https://github.com/omwah/pixy_rpi
    static final byte PIXY_SYNC_BYTE = 0x5a;
    static final byte PIXY_SYNC_BYTE_DATA = 0x5b;
    private final int RES_X;
    static final int PIXY_OUTBUF_SIZE = 6;
    static final int PIXY_MAXIMUM_ARRAYSIZE = 130;
    static final int PIXY_START_WORD = 0xaa55;
    static final int PIXY_START_WORDX = 0x55aa;
    static final int BLOCK_LEN = 5;
    static final int PIXY_SIGNATURE_NUM = 1;

    //private final ArrayList<ArrayList<BoundingBox>> PACKETS;
    public final ArrayList<BoundingBox> BOXES;
    private ArrayDeque<Byte> outBuf = new ArrayDeque<>(); // Future use for sending commands to Pixy.

    private boolean skipStart = false;
    private int debug = 0; // 0 - none, 1 - SmartDashboard, 2 - log to console/file

    private long getWord = 0;
    private long getStart = 0;
    private long getBlock = 0;
    private long readPackets = 0;

    public PixySPI(int spiPort) {
        // Only use ports [0 - 3]
        spiPort = (int) MercMath.clamp(spiPort, 0, 3);

        Port pValue = Port.kOnboardCS0;
        try {
            pValue = Port.valueOf("kOnboardCS" + spiPort);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SPI = new SPI(pValue);

        BOXES = new ArrayList<>();

        // Set some SPI parameters.
        SPI.setMSBFirst();
        SPI.setChipSelectActiveLow();
        SPI.setClockRate(1000);
        SPI.setSampleDataOnFalling();
        SPI.setClockActiveLow();

        RES_X = SensorsSettings.getCameraResolution().width;

    }

    /**
     * Gets object displacement from center of camera
     *
     * @return RES_X / 2 - BOXES.get(0).getCenterX()
     */
    public double getDisplacement() {
        double val = Double.NEGATIVE_INFINITY;

        if (!BOXES.isEmpty())
            val = RES_X / 2 - BOXES.get(0).getX();

        return val;
    }

    /**
     * Reads from SPI for data "words," and parses
     * all words into bounding boxes.
     *
     * @param max max number of boxes to find
     */
    public void getBoxes(int max) {
        // Clear out BOXES array list for reuse.
        BOXES.clear();
        long count = 0;

        // If we haven't found the start of a block, find it.
        if (!skipStart) {
            // If we can't find the start of a block, drop out.
            if (!getStart())
                return;
        } else {
            // Clear flag that tells us to find the next block as the logic below will loop
            // the appropriate number of times to retrieve a complete block.
            skipStart = false;
        }

        // Loop until we hit the maximum size allowed for BOXES, or until we know we have a complete setClawState of BOXES.
        while (BOXES.size() < max && BOXES.size() < PIXY_MAXIMUM_ARRAYSIZE) {
            // Since this is our first time in, bytes 2 and 3 are the checksum, grab them and store for future use.
            // NOTE: getWord grabs the entire 16 bits in one shot.
            int checksum = getWord();
            int trialsum = 0;

            // See if the checksum is really the beginning of the next block,
            // in which case return the current setClawState of BOXES found and setClawState the flag
            // to skip looking for the beginning of the next block since we already found it.
            if (checksum == PIXY_START_WORD) {
                skipStart = true;
                return;
            }
            // See if we received a empty buffer, if so, assume end of comms for now and return what we have.
            else if (checksum == 0)
                return;

            // Start constructing BOXES
            // Only need 5 slots since the first 3 slots, the double start BOXES and checksum, have been retrieved already.
            int[] box = new int[5];
            for (int i = 0; i < BLOCK_LEN; i++) {
                box[i] = getWord();
                trialsum += box[i];
            }

            // See if we received the data correctly.
            // Also make sure the target is for the first signature
            if (checksum == trialsum && box[0] == PIXY_SIGNATURE_NUM) {
                BoundingBox bound = new BoundingBox(
                    box[1],
                    box[2],
                    box[3],
                    box[4]
                );
                // Data has been validated, add the current block of data to the overall BOXES buffer.
                BOXES.add(bound);
            }

            // Check the next word from the Pixy to confirm it's the start of the next block.
            // Pixy sends two aa55 words at start of block, this should pull the first one.
            // The top of the loop should pull the other one.
            int w = getWord();

            if (w != PIXY_START_WORD) {
                // Sort array before returning
                BOXES.sort(BoundingBox::compareTo);

                return;
            }
        }

        // Should never get here, but if we happen to get a massive number of BOXES
        // and exceed the limit it will happen. In that case something is wrong
        // or you have a super natural Pixy and SPI link.
        log.log(Level.WARN, "Massive number of boxes!");
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        // Don't use
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return getDisplacement();
    }

    // region Comms
    // Pixy SPI comm functions derived from https://github.com/omwah/pixy_rpi
    private int getWord() {
        int word = 0x00;
        int ret = -1;

        ByteBuffer writeBuf = ByteBuffer.allocateDirect(2);
        writeBuf.order(ByteOrder.BIG_ENDIAN);

        ByteBuffer readBuf = ByteBuffer.allocateDirect(2);
        readBuf.order(ByteOrder.BIG_ENDIAN);

        if (outBuf.size() > 0)
            writeBuf.put(PIXY_SYNC_BYTE_DATA);
        else
            writeBuf.put(PIXY_SYNC_BYTE);

        // Flip the writeBuf so it's ready to be read.
        writeBuf.flip();

        // Send the sync / data bit / 0 to get the Pixy to return data appropriately.
        ret = SPI.transaction(writeBuf, readBuf, 2);

        // Set the position back to 0 in the buffer so we read it from the beginning next time.
        readBuf.rewind();

        // Store the contents of the buffer in a int that will be returned to the caller.
        word = (int) (readBuf.getShort() & 0xffff);

        // Clear the buffers, not needed, but nice to know they are cleaned out.
        writeBuf.clear();
        readBuf.clear();
        return (word);
    }

    // Need to come back to this one, used only for send control data to Pixy.
    private int send(byte data) {
        return (0);
    }

    private boolean getStart() {
        int lastw = 0xff;
        int count = 0;

        if (debug >= 1) {
            SmartDashboard.putNumber("getStart: count: ", getStart++);
        }

        // Loop until we get a start word from the Pixy.
        while (true) {
            int w = getWord();

            if ((w == 0x00) && (lastw == 0x00))
                // Could delay a bit to give time for next data block, but to get accurate time would tie up cpu.
                // So might as well return and let caller call this getStart again.
                return false;
            else if ((int) w == PIXY_START_WORD && (int) lastw == PIXY_START_WORD)
                return true;

            lastw = w;
        }
    }

    // Call this from readPackets, it's there commented out already.
    // Read the warning there as well.
    // This will dump all data to the console/log and SmartDashboard
    // Set the debug level accordingly, otherwise it's going to be a bit boring
    // watching nothing happen while your RoboRio is screaming into the void.
    public void rawComms() {
        //logger.entering(getClass().getName(), "doIt");
        int ret = -1;
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.order(ByteOrder.BIG_ENDIAN);
        ByteBuffer writeBuf = ByteBuffer.allocateDirect(2);
        writeBuf.order(ByteOrder.BIG_ENDIAN);
        ByteBuffer readBuf = ByteBuffer.allocateDirect(2);
        readBuf.order(ByteOrder.BIG_ENDIAN);
        String readString = null;
        String writeString = null;

        while (true) {
            writeBuf.put(PIXY_SYNC_BYTE);
            writeBuf.flip();
            writeString = MercMath.bbToString(writeBuf);

            ret = SPI.transaction(writeBuf, readBuf, 2);
            //readBuf.flip();

            readString = MercMath.bbToString(readBuf);
            readBuf.rewind();

            writeBuf.clear();
            readBuf.clear();
        }
    }
    // endregion
}

