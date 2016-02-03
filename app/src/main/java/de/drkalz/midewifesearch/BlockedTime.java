package de.drkalz.midewifesearch;

import java.util.Date;

public class BlockedTime {

    private String midwifeID;
    private Date startOfBlock;
    private Date endOfBlock;

    public BlockedTime() {
    }

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
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
