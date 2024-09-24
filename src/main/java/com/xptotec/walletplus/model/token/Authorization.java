package com.xptotec.walletplus.model.token;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Authorization implements Serializable {

    private static final long serialVersionUID = -1227402016334916821L;
    private Token token;
}
