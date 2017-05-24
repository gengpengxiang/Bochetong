package com.bolong.bochetong.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by admin on 2017/4/28.
 */
@Entity
public class CarPlate {
    @Id
    private Long id;
    private String carPlate;

    public CarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    @Generated(hash = 1665323809)

    public CarPlate(Long id, String carPlate) {
        this.id = id;
        this.carPlate = carPlate;
    }

    @Generated(hash = 731998766)
    public CarPlate() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarPlate() {
        return this.carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

}
