package ru.flippy.skyscrapers.sdk.api.model;

import java.util.List;

public class Vendor {
    private List<VendorBuff> buffs;
    private List<VendorAuto> autos;

    public List<VendorBuff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<VendorBuff> buffs) {
        this.buffs = buffs;
    }

    public List<VendorAuto> getAutos() {
        return autos;
    }

    public void setAutos(List<VendorAuto> autos) {
        this.autos = autos;
    }
}
