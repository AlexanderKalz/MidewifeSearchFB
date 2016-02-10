package de.drkalz.midwifesearch.Midwifes;

import java.util.Date;

public class BlockedTime {

    private Date startOfBlock;
    private Date endOfBlock;
    private String midwifeID;

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

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
    }
}
