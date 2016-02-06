package de.drkalz.midewifesearch.PoJoS;

import java.util.Date;

public class BlockedTime {

    private Date startOfBlock;
    private Date endOfBlock;

    public BlockedTime() {
    }

    public Date getStartOfBlock() {
        return startOfBlock;
    }

    public void setStartOfBlock(Date startOfBlock) {
        this.startOfBlock = startOfBlock;
    }

    public Date getEndOfBlock() {
        return endOfBlock;
    }

    public void setEndOfBlock(Date endOfBlock) {
        this.endOfBlock = endOfBlock;
    }
}
