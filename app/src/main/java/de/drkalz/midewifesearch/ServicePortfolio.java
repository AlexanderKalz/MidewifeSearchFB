package de.drkalz.midewifesearch;

/**
 * Created by Alex on 30.01.16.
 */
public class ServicePortfolio {

    private boolean beleggeburt, geburtsvorbereitung, geburt_hge, hausgeburt, rueckbildungskurs,
            schwangerenvorsorge, wochenbetreueung,
            german, english, french, spanish;
    private String midWifeID;

    public ServicePortfolio() {
    }

    public boolean isGerman() {
        return german;
    }

    public void setGerman(boolean german) {
        this.german = german;
    }

    public boolean isEnglish() {
        return english;
    }

    public void setEnglish(boolean english) {
        this.english = english;
    }

    public boolean isFrench() {
        return french;
    }

    public void setFrench(boolean french) {
        this.french = french;
    }

    public boolean isSpanish() {
        return spanish;
    }

    public void setSpanish(boolean spanish) {
        this.spanish = spanish;
    }

    public boolean isBeleggeburt() {
        return beleggeburt;
    }

    public void setBeleggeburt(boolean beleggeburt) {
        this.beleggeburt = beleggeburt;
    }

    public boolean isGeburtsvorbereitung() {
        return geburtsvorbereitung;
    }

    public void setGeburtsvorbereitung(boolean geburtsvorbereitung) {
        this.geburtsvorbereitung = geburtsvorbereitung;
    }

    public boolean isGeburt_hge() {
        return geburt_hge;
    }

    public void setGeburt_hge(boolean geburt_hge) {
        this.geburt_hge = geburt_hge;
    }

    public boolean isHausgeburt() {
        return hausgeburt;
    }

    public void setHausgeburt(boolean hausgeburt) {
        this.hausgeburt = hausgeburt;
    }

    public boolean isRueckbildungskurs() {
        return rueckbildungskurs;
    }

    public void setRueckbildungskurs(boolean rueckbildungskurs) {
        this.rueckbildungskurs = rueckbildungskurs;
    }

    public boolean isSchwangerenvorsorge() {
        return schwangerenvorsorge;
    }

    public void setSchwangerenvorsorge(boolean schwangerenvorsorge) {
        this.schwangerenvorsorge = schwangerenvorsorge;
    }

    public boolean isWochenbetreueung() {
        return wochenbetreueung;
    }

    public void setWochenbetreueung(boolean wochenbetreueung) {
        this.wochenbetreueung = wochenbetreueung;
    }

    public String getMidWifeID() {
        return midWifeID;
    }

    public void setMidWifeID(String midWifeID) {
        this.midWifeID = midWifeID;
    }
}
