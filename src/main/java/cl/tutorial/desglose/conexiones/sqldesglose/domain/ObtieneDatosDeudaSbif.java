package cl.tutorial.desglose.conexiones.sqldesglose.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class ObtieneDatosDeudaSbif implements Serializable {

    @Id
    private String GLSDEUDA_01;
    private String GLSDEUDA_02;
    private String GLSDEUDA_03;
    private String GLSDEUDA_04;
    private String GLSDEUDA_05;
    private String GLSDEUDA_06;
    private String GLSDEUDA_07;
    private String GLSDEUDA_08;
    private String GLSDEUDA_09;
    private String GLSDEUDA_10;
    private String GLSDEUDA_11;
    private String GLSDEUDA_12;
    private String GLSDEUDA_13;
    private String GLSDEUDA_14;
    private String GLSDEUDA_15;
    private String GLSDEUDA_16;
    private String GLSDEUDA_17;
    private String GLSDEUDA_18;
    private String GLSDEUDA_19;
    private String GLSDEUDA_21;
    private String GLSDEUDA_22;
    private BigDecimal PERIODO01_1;
    private BigDecimal PERIODO01_2;
    private BigDecimal PERIODO01_3;
    private BigDecimal PERIODO01_4;
    private BigDecimal DEUDA_DIREC_VIG02_1;
    private BigDecimal DEUDA_DIREC_VIG02_2;
    private BigDecimal DEUDA_DIREC_VIG02_3;
    private BigDecimal DEUDA_DIREC_VIG02_4;
    private BigDecimal DEUDA_X_INV_FINAN03_1;
    private BigDecimal DEUDA_X_INV_FINAN03_2;
    private BigDecimal DEUDA_X_INV_FINAN03_3;
    private BigDecimal DEUDA_X_INV_FINAN03_4;
    private BigDecimal DEUDA_VENCI_DIREC04_1;
    private BigDecimal DEUDA_VENCI_DIREC04_2;
    private BigDecimal DEUDA_VENCI_DIREC04_3;
    private BigDecimal DEUDA_VENCI_DIREC04_4;
    private BigDecimal DEUDA_X_OPE_C_PACTO05_1;
    private BigDecimal DEUDA_X_OPE_C_PACTO05_2;
    private BigDecimal DEUDA_X_OPE_C_PACTO05_3;
    private BigDecimal DEUDA_X_OPE_C_PACTO05_4;
    private BigDecimal DEUDA_INDIREC_VIG06_1;
    private BigDecimal DEUDA_INDIREC_VIG06_2;
    private BigDecimal DEUDA_INDIREC_VIG06_3;
    private BigDecimal DEUDA_INDIREC_VIG06_4;
    private BigDecimal DEUDA_INDIREC_VENCI07_1;
    private BigDecimal DEUDA_INDIREC_VENCI07_2;
    private BigDecimal DEUDA_INDIREC_VENCI07_3;
    private BigDecimal DEUDA_INDIREC_VENCI07_4;
    private BigDecimal DEUDA_COMERCIAL08_1;
    private BigDecimal DEUDA_COMERCIAL08_2;
    private BigDecimal DEUDA_COMERCIAL08_3;
    private BigDecimal DEUDA_COMERCIAL08_4;
    private BigDecimal DEUDA_X_CRED_CONSUMO09_1;
    private BigDecimal DEUDA_X_CRED_CONSUMO09_2;
    private BigDecimal DEUDA_X_CRED_CONSUMO09_3;
    private BigDecimal DEUDA_X_CRED_CONSUMO09_4;
    private BigDecimal NRO_ACREE_X_CRED_CONSUMO10_1;
    private BigDecimal NRO_ACREE_X_CRED_CONSUMO10_2;
    private BigDecimal NRO_ACREE_X_CRED_CONSUMO10_3;
    private BigDecimal NRO_ACREE_X_CRED_CONSUMO10_4;
    private BigDecimal DEUDA_X_CRED_HIPOTECA11_1;
    private BigDecimal DEUDA_X_CRED_HIPOTECA11_2;
    private BigDecimal DEUDA_X_CRED_HIPOTECA11_3;
    private BigDecimal DEUDA_X_CRED_HIPOTECA11_4;
    private BigDecimal DEUDA_MOROSA12_1;
    private BigDecimal DEUDA_MOROSA12_2;
    private BigDecimal DEUDA_MOROSA12_3;
    private BigDecimal DEUDA_MOROSA12_4;
    private BigDecimal DEUDA_CAST_DIRECTA13_1;
    private BigDecimal DEUDA_CAST_DIRECTA13_2;
    private BigDecimal DEUDA_CAST_DIRECTA13_3;
    private BigDecimal DEUDA_CAST_DIRECTA13_4;
    private BigDecimal DEUDA_CAST_INDIRECTA14_1;
    private BigDecimal DEUDA_CAST_INDIRECTA14_2;
    private BigDecimal DEUDA_CAST_INDIRECTA14_3;
    private BigDecimal DEUDA_CAST_INDIRECTA14_4;
    private BigDecimal LINEA_CRED_DISPONIBLE15_1;
    private BigDecimal LINEA_CRED_DISPONIBLE15_2;
    private BigDecimal LINEA_CRED_DISPONIBLE15_3;
    private BigDecimal LINEA_CRED_DISPONIBLE15_4;
    private BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_1;
    private BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_2;
    private BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_3;
    private BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_4;
    private BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_1;
    private BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_2;
    private BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_3;
    private BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_4;
    private BigDecimal NRO_INST_CRED_COM18_1;
    private BigDecimal NRO_INST_CRED_COM18_2;
    private BigDecimal NRO_INST_CRED_COM18_3;
    private BigDecimal NRO_INST_CRED_COM18_4;
    private BigDecimal DEUDA_LEASING19_1;
    private BigDecimal DEUDA_LEASING19_2;
    private BigDecimal DEUDA_LEASING19_3;
    private BigDecimal DEUDA_LEASING19_4;
    private BigDecimal DEUDA_MOROSA_LEASING20_1;
    private BigDecimal DEUDA_MOROSA_LEASING20_2;
    private BigDecimal DEUDA_MOROSA_LEASING20_3;
    private BigDecimal DEUDA_MOROSA_LEASING20_4;
    private BigDecimal DEUDA_CRED_CONTINGENTES21_1;
    private BigDecimal DEUDA_CRED_CONTINGENTES21_2;
    private BigDecimal DEUDA_CRED_CONTINGENTES21_3;
    private BigDecimal DEUDA_CRED_CONTINGENTES21_4;
    private BigDecimal DEUDA_DIR_90_18022_1;
    private BigDecimal DEUDA_DIR_90_18022_2;
    private BigDecimal DEUDA_DIR_90_18022_3;
    private BigDecimal DEUDA_DIR_90_18022_4;

    public String getGLSDEUDA_01() {
        return GLSDEUDA_01;
    }

    public void setGLSDEUDA_01(String GLSDEUDA_01) {
        this.GLSDEUDA_01 = GLSDEUDA_01;
    }

    public String getGLSDEUDA_02() {
        return GLSDEUDA_02;
    }

    public void setGLSDEUDA_02(String GLSDEUDA_02) {
        this.GLSDEUDA_02 = GLSDEUDA_02;
    }

    public String getGLSDEUDA_03() {
        return GLSDEUDA_03;
    }

    public void setGLSDEUDA_03(String GLSDEUDA_03) {
        this.GLSDEUDA_03 = GLSDEUDA_03;
    }

    public String getGLSDEUDA_04() {
        return GLSDEUDA_04;
    }

    public void setGLSDEUDA_04(String GLSDEUDA_04) {
        this.GLSDEUDA_04 = GLSDEUDA_04;
    }

    public String getGLSDEUDA_05() {
        return GLSDEUDA_05;
    }

    public void setGLSDEUDA_05(String GLSDEUDA_05) {
        this.GLSDEUDA_05 = GLSDEUDA_05;
    }

    public String getGLSDEUDA_06() {
        return GLSDEUDA_06;
    }

    public void setGLSDEUDA_06(String GLSDEUDA_06) {
        this.GLSDEUDA_06 = GLSDEUDA_06;
    }

    public String getGLSDEUDA_07() {
        return GLSDEUDA_07;
    }

    public void setGLSDEUDA_07(String GLSDEUDA_07) {
        this.GLSDEUDA_07 = GLSDEUDA_07;
    }

    public String getGLSDEUDA_08() {
        return GLSDEUDA_08;
    }

    public void setGLSDEUDA_08(String GLSDEUDA_08) {
        this.GLSDEUDA_08 = GLSDEUDA_08;
    }

    public String getGLSDEUDA_09() {
        return GLSDEUDA_09;
    }

    public void setGLSDEUDA_09(String GLSDEUDA_09) {
        this.GLSDEUDA_09 = GLSDEUDA_09;
    }

    public String getGLSDEUDA_10() {
        return GLSDEUDA_10;
    }

    public void setGLSDEUDA_10(String GLSDEUDA_10) {
        this.GLSDEUDA_10 = GLSDEUDA_10;
    }

    public String getGLSDEUDA_11() {
        return GLSDEUDA_11;
    }

    public void setGLSDEUDA_11(String GLSDEUDA_11) {
        this.GLSDEUDA_11 = GLSDEUDA_11;
    }

    public String getGLSDEUDA_12() {
        return GLSDEUDA_12;
    }

    public void setGLSDEUDA_12(String GLSDEUDA_12) {
        this.GLSDEUDA_12 = GLSDEUDA_12;
    }

    public String getGLSDEUDA_13() {
        return GLSDEUDA_13;
    }

    public void setGLSDEUDA_13(String GLSDEUDA_13) {
        this.GLSDEUDA_13 = GLSDEUDA_13;
    }

    public String getGLSDEUDA_14() {
        return GLSDEUDA_14;
    }

    public void setGLSDEUDA_14(String GLSDEUDA_14) {
        this.GLSDEUDA_14 = GLSDEUDA_14;
    }

    public String getGLSDEUDA_15() {
        return GLSDEUDA_15;
    }

    public void setGLSDEUDA_15(String GLSDEUDA_15) {
        this.GLSDEUDA_15 = GLSDEUDA_15;
    }

    public String getGLSDEUDA_16() {
        return GLSDEUDA_16;
    }

    public void setGLSDEUDA_16(String GLSDEUDA_16) {
        this.GLSDEUDA_16 = GLSDEUDA_16;
    }

    public String getGLSDEUDA_17() {
        return GLSDEUDA_17;
    }

    public void setGLSDEUDA_17(String GLSDEUDA_17) {
        this.GLSDEUDA_17 = GLSDEUDA_17;
    }

    public String getGLSDEUDA_18() {
        return GLSDEUDA_18;
    }

    public void setGLSDEUDA_18(String GLSDEUDA_18) {
        this.GLSDEUDA_18 = GLSDEUDA_18;
    }

    public String getGLSDEUDA_19() {
        return GLSDEUDA_19;
    }

    public void setGLSDEUDA_19(String GLSDEUDA_19) {
        this.GLSDEUDA_19 = GLSDEUDA_19;
    }

    public String getGLSDEUDA_21() {
        return GLSDEUDA_21;
    }

    public void setGLSDEUDA_21(String GLSDEUDA_21) {
        this.GLSDEUDA_21 = GLSDEUDA_21;
    }

    public String getGLSDEUDA_22() {
        return GLSDEUDA_22;
    }

    public void setGLSDEUDA_22(String GLSDEUDA_22) {
        this.GLSDEUDA_22 = GLSDEUDA_22;
    }

    public BigDecimal getPERIODO01_1() {
        return PERIODO01_1;
    }

    public void setPERIODO01_1(BigDecimal PERIODO01_1) {
        this.PERIODO01_1 = PERIODO01_1;
    }

    public BigDecimal getPERIODO01_2() {
        return PERIODO01_2;
    }

    public void setPERIODO01_2(BigDecimal PERIODO01_2) {
        this.PERIODO01_2 = PERIODO01_2;
    }

    public BigDecimal getPERIODO01_3() {
        return PERIODO01_3;
    }

    public void setPERIODO01_3(BigDecimal PERIODO01_3) {
        this.PERIODO01_3 = PERIODO01_3;
    }

    public BigDecimal getPERIODO01_4() {
        return PERIODO01_4;
    }

    public void setPERIODO01_4(BigDecimal PERIODO01_4) {
        this.PERIODO01_4 = PERIODO01_4;
    }

    public BigDecimal getDEUDA_DIREC_VIG02_1() {
        return DEUDA_DIREC_VIG02_1;
    }

    public void setDEUDA_DIREC_VIG02_1(BigDecimal DEUDA_DIREC_VIG02_1) {
        this.DEUDA_DIREC_VIG02_1 = DEUDA_DIREC_VIG02_1;
    }

    public BigDecimal getDEUDA_DIREC_VIG02_2() {
        return DEUDA_DIREC_VIG02_2;
    }

    public void setDEUDA_DIREC_VIG02_2(BigDecimal DEUDA_DIREC_VIG02_2) {
        this.DEUDA_DIREC_VIG02_2 = DEUDA_DIREC_VIG02_2;
    }

    public BigDecimal getDEUDA_DIREC_VIG02_3() {
        return DEUDA_DIREC_VIG02_3;
    }

    public void setDEUDA_DIREC_VIG02_3(BigDecimal DEUDA_DIREC_VIG02_3) {
        this.DEUDA_DIREC_VIG02_3 = DEUDA_DIREC_VIG02_3;
    }

    public BigDecimal getDEUDA_DIREC_VIG02_4() {
        return DEUDA_DIREC_VIG02_4;
    }

    public void setDEUDA_DIREC_VIG02_4(BigDecimal DEUDA_DIREC_VIG02_4) {
        this.DEUDA_DIREC_VIG02_4 = DEUDA_DIREC_VIG02_4;
    }

    public BigDecimal getDEUDA_X_INV_FINAN03_1() {
        return DEUDA_X_INV_FINAN03_1;
    }

    public void setDEUDA_X_INV_FINAN03_1(BigDecimal DEUDA_X_INV_FINAN03_1) {
        this.DEUDA_X_INV_FINAN03_1 = DEUDA_X_INV_FINAN03_1;
    }

    public BigDecimal getDEUDA_X_INV_FINAN03_2() {
        return DEUDA_X_INV_FINAN03_2;
    }

    public void setDEUDA_X_INV_FINAN03_2(BigDecimal DEUDA_X_INV_FINAN03_2) {
        this.DEUDA_X_INV_FINAN03_2 = DEUDA_X_INV_FINAN03_2;
    }

    public BigDecimal getDEUDA_X_INV_FINAN03_3() {
        return DEUDA_X_INV_FINAN03_3;
    }

    public void setDEUDA_X_INV_FINAN03_3(BigDecimal DEUDA_X_INV_FINAN03_3) {
        this.DEUDA_X_INV_FINAN03_3 = DEUDA_X_INV_FINAN03_3;
    }

    public BigDecimal getDEUDA_X_INV_FINAN03_4() {
        return DEUDA_X_INV_FINAN03_4;
    }

    public void setDEUDA_X_INV_FINAN03_4(BigDecimal DEUDA_X_INV_FINAN03_4) {
        this.DEUDA_X_INV_FINAN03_4 = DEUDA_X_INV_FINAN03_4;
    }

    public BigDecimal getDEUDA_VENCI_DIREC04_1() {
        return DEUDA_VENCI_DIREC04_1;
    }

    public void setDEUDA_VENCI_DIREC04_1(BigDecimal DEUDA_VENCI_DIREC04_1) {
        this.DEUDA_VENCI_DIREC04_1 = DEUDA_VENCI_DIREC04_1;
    }

    public BigDecimal getDEUDA_VENCI_DIREC04_2() {
        return DEUDA_VENCI_DIREC04_2;
    }

    public void setDEUDA_VENCI_DIREC04_2(BigDecimal DEUDA_VENCI_DIREC04_2) {
        this.DEUDA_VENCI_DIREC04_2 = DEUDA_VENCI_DIREC04_2;
    }

    public BigDecimal getDEUDA_VENCI_DIREC04_3() {
        return DEUDA_VENCI_DIREC04_3;
    }

    public void setDEUDA_VENCI_DIREC04_3(BigDecimal DEUDA_VENCI_DIREC04_3) {
        this.DEUDA_VENCI_DIREC04_3 = DEUDA_VENCI_DIREC04_3;
    }

    public BigDecimal getDEUDA_VENCI_DIREC04_4() {
        return DEUDA_VENCI_DIREC04_4;
    }

    public void setDEUDA_VENCI_DIREC04_4(BigDecimal DEUDA_VENCI_DIREC04_4) {
        this.DEUDA_VENCI_DIREC04_4 = DEUDA_VENCI_DIREC04_4;
    }

    public BigDecimal getDEUDA_X_OPE_C_PACTO05_1() {
        return DEUDA_X_OPE_C_PACTO05_1;
    }

    public void setDEUDA_X_OPE_C_PACTO05_1(BigDecimal DEUDA_X_OPE_C_PACTO05_1) {
        this.DEUDA_X_OPE_C_PACTO05_1 = DEUDA_X_OPE_C_PACTO05_1;
    }

    public BigDecimal getDEUDA_X_OPE_C_PACTO05_2() {
        return DEUDA_X_OPE_C_PACTO05_2;
    }

    public void setDEUDA_X_OPE_C_PACTO05_2(BigDecimal DEUDA_X_OPE_C_PACTO05_2) {
        this.DEUDA_X_OPE_C_PACTO05_2 = DEUDA_X_OPE_C_PACTO05_2;
    }

    public BigDecimal getDEUDA_X_OPE_C_PACTO05_3() {
        return DEUDA_X_OPE_C_PACTO05_3;
    }

    public void setDEUDA_X_OPE_C_PACTO05_3(BigDecimal DEUDA_X_OPE_C_PACTO05_3) {
        this.DEUDA_X_OPE_C_PACTO05_3 = DEUDA_X_OPE_C_PACTO05_3;
    }

    public BigDecimal getDEUDA_X_OPE_C_PACTO05_4() {
        return DEUDA_X_OPE_C_PACTO05_4;
    }

    public void setDEUDA_X_OPE_C_PACTO05_4(BigDecimal DEUDA_X_OPE_C_PACTO05_4) {
        this.DEUDA_X_OPE_C_PACTO05_4 = DEUDA_X_OPE_C_PACTO05_4;
    }

    public BigDecimal getDEUDA_INDIREC_VIG06_1() {
        return DEUDA_INDIREC_VIG06_1;
    }

    public void setDEUDA_INDIREC_VIG06_1(BigDecimal DEUDA_INDIREC_VIG06_1) {
        this.DEUDA_INDIREC_VIG06_1 = DEUDA_INDIREC_VIG06_1;
    }

    public BigDecimal getDEUDA_INDIREC_VIG06_2() {
        return DEUDA_INDIREC_VIG06_2;
    }

    public void setDEUDA_INDIREC_VIG06_2(BigDecimal DEUDA_INDIREC_VIG06_2) {
        this.DEUDA_INDIREC_VIG06_2 = DEUDA_INDIREC_VIG06_2;
    }

    public BigDecimal getDEUDA_INDIREC_VIG06_3() {
        return DEUDA_INDIREC_VIG06_3;
    }

    public void setDEUDA_INDIREC_VIG06_3(BigDecimal DEUDA_INDIREC_VIG06_3) {
        this.DEUDA_INDIREC_VIG06_3 = DEUDA_INDIREC_VIG06_3;
    }

    public BigDecimal getDEUDA_INDIREC_VIG06_4() {
        return DEUDA_INDIREC_VIG06_4;
    }

    public void setDEUDA_INDIREC_VIG06_4(BigDecimal DEUDA_INDIREC_VIG06_4) {
        this.DEUDA_INDIREC_VIG06_4 = DEUDA_INDIREC_VIG06_4;
    }

    public BigDecimal getDEUDA_INDIREC_VENCI07_1() {
        return DEUDA_INDIREC_VENCI07_1;
    }

    public void setDEUDA_INDIREC_VENCI07_1(BigDecimal DEUDA_INDIREC_VENCI07_1) {
        this.DEUDA_INDIREC_VENCI07_1 = DEUDA_INDIREC_VENCI07_1;
    }

    public BigDecimal getDEUDA_INDIREC_VENCI07_2() {
        return DEUDA_INDIREC_VENCI07_2;
    }

    public void setDEUDA_INDIREC_VENCI07_2(BigDecimal DEUDA_INDIREC_VENCI07_2) {
        this.DEUDA_INDIREC_VENCI07_2 = DEUDA_INDIREC_VENCI07_2;
    }

    public BigDecimal getDEUDA_INDIREC_VENCI07_3() {
        return DEUDA_INDIREC_VENCI07_3;
    }

    public void setDEUDA_INDIREC_VENCI07_3(BigDecimal DEUDA_INDIREC_VENCI07_3) {
        this.DEUDA_INDIREC_VENCI07_3 = DEUDA_INDIREC_VENCI07_3;
    }

    public BigDecimal getDEUDA_INDIREC_VENCI07_4() {
        return DEUDA_INDIREC_VENCI07_4;
    }

    public void setDEUDA_INDIREC_VENCI07_4(BigDecimal DEUDA_INDIREC_VENCI07_4) {
        this.DEUDA_INDIREC_VENCI07_4 = DEUDA_INDIREC_VENCI07_4;
    }

    public BigDecimal getDEUDA_COMERCIAL08_1() {
        return DEUDA_COMERCIAL08_1;
    }

    public void setDEUDA_COMERCIAL08_1(BigDecimal DEUDA_COMERCIAL08_1) {
        this.DEUDA_COMERCIAL08_1 = DEUDA_COMERCIAL08_1;
    }

    public BigDecimal getDEUDA_COMERCIAL08_2() {
        return DEUDA_COMERCIAL08_2;
    }

    public void setDEUDA_COMERCIAL08_2(BigDecimal DEUDA_COMERCIAL08_2) {
        this.DEUDA_COMERCIAL08_2 = DEUDA_COMERCIAL08_2;
    }

    public BigDecimal getDEUDA_COMERCIAL08_3() {
        return DEUDA_COMERCIAL08_3;
    }

    public void setDEUDA_COMERCIAL08_3(BigDecimal DEUDA_COMERCIAL08_3) {
        this.DEUDA_COMERCIAL08_3 = DEUDA_COMERCIAL08_3;
    }

    public BigDecimal getDEUDA_COMERCIAL08_4() {
        return DEUDA_COMERCIAL08_4;
    }

    public void setDEUDA_COMERCIAL08_4(BigDecimal DEUDA_COMERCIAL08_4) {
        this.DEUDA_COMERCIAL08_4 = DEUDA_COMERCIAL08_4;
    }

    public BigDecimal getDEUDA_X_CRED_CONSUMO09_1() {
        return DEUDA_X_CRED_CONSUMO09_1;
    }

    public void setDEUDA_X_CRED_CONSUMO09_1(BigDecimal DEUDA_X_CRED_CONSUMO09_1) {
        this.DEUDA_X_CRED_CONSUMO09_1 = DEUDA_X_CRED_CONSUMO09_1;
    }

    public BigDecimal getDEUDA_X_CRED_CONSUMO09_2() {
        return DEUDA_X_CRED_CONSUMO09_2;
    }

    public void setDEUDA_X_CRED_CONSUMO09_2(BigDecimal DEUDA_X_CRED_CONSUMO09_2) {
        this.DEUDA_X_CRED_CONSUMO09_2 = DEUDA_X_CRED_CONSUMO09_2;
    }

    public BigDecimal getDEUDA_X_CRED_CONSUMO09_3() {
        return DEUDA_X_CRED_CONSUMO09_3;
    }

    public void setDEUDA_X_CRED_CONSUMO09_3(BigDecimal DEUDA_X_CRED_CONSUMO09_3) {
        this.DEUDA_X_CRED_CONSUMO09_3 = DEUDA_X_CRED_CONSUMO09_3;
    }

    public BigDecimal getDEUDA_X_CRED_CONSUMO09_4() {
        return DEUDA_X_CRED_CONSUMO09_4;
    }

    public void setDEUDA_X_CRED_CONSUMO09_4(BigDecimal DEUDA_X_CRED_CONSUMO09_4) {
        this.DEUDA_X_CRED_CONSUMO09_4 = DEUDA_X_CRED_CONSUMO09_4;
    }

    public BigDecimal getNRO_ACREE_X_CRED_CONSUMO10_1() {
        return NRO_ACREE_X_CRED_CONSUMO10_1;
    }

    public void setNRO_ACREE_X_CRED_CONSUMO10_1(BigDecimal NRO_ACREE_X_CRED_CONSUMO10_1) {
        this.NRO_ACREE_X_CRED_CONSUMO10_1 = NRO_ACREE_X_CRED_CONSUMO10_1;
    }

    public BigDecimal getNRO_ACREE_X_CRED_CONSUMO10_2() {
        return NRO_ACREE_X_CRED_CONSUMO10_2;
    }

    public void setNRO_ACREE_X_CRED_CONSUMO10_2(BigDecimal NRO_ACREE_X_CRED_CONSUMO10_2) {
        this.NRO_ACREE_X_CRED_CONSUMO10_2 = NRO_ACREE_X_CRED_CONSUMO10_2;
    }

    public BigDecimal getNRO_ACREE_X_CRED_CONSUMO10_3() {
        return NRO_ACREE_X_CRED_CONSUMO10_3;
    }

    public void setNRO_ACREE_X_CRED_CONSUMO10_3(BigDecimal NRO_ACREE_X_CRED_CONSUMO10_3) {
        this.NRO_ACREE_X_CRED_CONSUMO10_3 = NRO_ACREE_X_CRED_CONSUMO10_3;
    }

    public BigDecimal getNRO_ACREE_X_CRED_CONSUMO10_4() {
        return NRO_ACREE_X_CRED_CONSUMO10_4;
    }

    public void setNRO_ACREE_X_CRED_CONSUMO10_4(BigDecimal NRO_ACREE_X_CRED_CONSUMO10_4) {
        this.NRO_ACREE_X_CRED_CONSUMO10_4 = NRO_ACREE_X_CRED_CONSUMO10_4;
    }

    public BigDecimal getDEUDA_X_CRED_HIPOTECA11_1() {
        return DEUDA_X_CRED_HIPOTECA11_1;
    }

    public void setDEUDA_X_CRED_HIPOTECA11_1(BigDecimal DEUDA_X_CRED_HIPOTECA11_1) {
        this.DEUDA_X_CRED_HIPOTECA11_1 = DEUDA_X_CRED_HIPOTECA11_1;
    }

    public BigDecimal getDEUDA_X_CRED_HIPOTECA11_2() {
        return DEUDA_X_CRED_HIPOTECA11_2;
    }

    public void setDEUDA_X_CRED_HIPOTECA11_2(BigDecimal DEUDA_X_CRED_HIPOTECA11_2) {
        this.DEUDA_X_CRED_HIPOTECA11_2 = DEUDA_X_CRED_HIPOTECA11_2;
    }

    public BigDecimal getDEUDA_X_CRED_HIPOTECA11_3() {
        return DEUDA_X_CRED_HIPOTECA11_3;
    }

    public void setDEUDA_X_CRED_HIPOTECA11_3(BigDecimal DEUDA_X_CRED_HIPOTECA11_3) {
        this.DEUDA_X_CRED_HIPOTECA11_3 = DEUDA_X_CRED_HIPOTECA11_3;
    }

    public BigDecimal getDEUDA_X_CRED_HIPOTECA11_4() {
        return DEUDA_X_CRED_HIPOTECA11_4;
    }

    public void setDEUDA_X_CRED_HIPOTECA11_4(BigDecimal DEUDA_X_CRED_HIPOTECA11_4) {
        this.DEUDA_X_CRED_HIPOTECA11_4 = DEUDA_X_CRED_HIPOTECA11_4;
    }

    public BigDecimal getDEUDA_MOROSA12_1() {
        return DEUDA_MOROSA12_1;
    }

    public void setDEUDA_MOROSA12_1(BigDecimal DEUDA_MOROSA12_1) {
        this.DEUDA_MOROSA12_1 = DEUDA_MOROSA12_1;
    }

    public BigDecimal getDEUDA_MOROSA12_2() {
        return DEUDA_MOROSA12_2;
    }

    public void setDEUDA_MOROSA12_2(BigDecimal DEUDA_MOROSA12_2) {
        this.DEUDA_MOROSA12_2 = DEUDA_MOROSA12_2;
    }

    public BigDecimal getDEUDA_MOROSA12_3() {
        return DEUDA_MOROSA12_3;
    }

    public void setDEUDA_MOROSA12_3(BigDecimal DEUDA_MOROSA12_3) {
        this.DEUDA_MOROSA12_3 = DEUDA_MOROSA12_3;
    }

    public BigDecimal getDEUDA_MOROSA12_4() {
        return DEUDA_MOROSA12_4;
    }

    public void setDEUDA_MOROSA12_4(BigDecimal DEUDA_MOROSA12_4) {
        this.DEUDA_MOROSA12_4 = DEUDA_MOROSA12_4;
    }

    public BigDecimal getDEUDA_CAST_DIRECTA13_1() {
        return DEUDA_CAST_DIRECTA13_1;
    }

    public void setDEUDA_CAST_DIRECTA13_1(BigDecimal DEUDA_CAST_DIRECTA13_1) {
        this.DEUDA_CAST_DIRECTA13_1 = DEUDA_CAST_DIRECTA13_1;
    }

    public BigDecimal getDEUDA_CAST_DIRECTA13_2() {
        return DEUDA_CAST_DIRECTA13_2;
    }

    public void setDEUDA_CAST_DIRECTA13_2(BigDecimal DEUDA_CAST_DIRECTA13_2) {
        this.DEUDA_CAST_DIRECTA13_2 = DEUDA_CAST_DIRECTA13_2;
    }

    public BigDecimal getDEUDA_CAST_DIRECTA13_3() {
        return DEUDA_CAST_DIRECTA13_3;
    }

    public void setDEUDA_CAST_DIRECTA13_3(BigDecimal DEUDA_CAST_DIRECTA13_3) {
        this.DEUDA_CAST_DIRECTA13_3 = DEUDA_CAST_DIRECTA13_3;
    }

    public BigDecimal getDEUDA_CAST_DIRECTA13_4() {
        return DEUDA_CAST_DIRECTA13_4;
    }

    public void setDEUDA_CAST_DIRECTA13_4(BigDecimal DEUDA_CAST_DIRECTA13_4) {
        this.DEUDA_CAST_DIRECTA13_4 = DEUDA_CAST_DIRECTA13_4;
    }

    public BigDecimal getDEUDA_CAST_INDIRECTA14_1() {
        return DEUDA_CAST_INDIRECTA14_1;
    }

    public void setDEUDA_CAST_INDIRECTA14_1(BigDecimal DEUDA_CAST_INDIRECTA14_1) {
        this.DEUDA_CAST_INDIRECTA14_1 = DEUDA_CAST_INDIRECTA14_1;
    }

    public BigDecimal getDEUDA_CAST_INDIRECTA14_2() {
        return DEUDA_CAST_INDIRECTA14_2;
    }

    public void setDEUDA_CAST_INDIRECTA14_2(BigDecimal DEUDA_CAST_INDIRECTA14_2) {
        this.DEUDA_CAST_INDIRECTA14_2 = DEUDA_CAST_INDIRECTA14_2;
    }

    public BigDecimal getDEUDA_CAST_INDIRECTA14_3() {
        return DEUDA_CAST_INDIRECTA14_3;
    }

    public void setDEUDA_CAST_INDIRECTA14_3(BigDecimal DEUDA_CAST_INDIRECTA14_3) {
        this.DEUDA_CAST_INDIRECTA14_3 = DEUDA_CAST_INDIRECTA14_3;
    }

    public BigDecimal getDEUDA_CAST_INDIRECTA14_4() {
        return DEUDA_CAST_INDIRECTA14_4;
    }

    public void setDEUDA_CAST_INDIRECTA14_4(BigDecimal DEUDA_CAST_INDIRECTA14_4) {
        this.DEUDA_CAST_INDIRECTA14_4 = DEUDA_CAST_INDIRECTA14_4;
    }

    public BigDecimal getLINEA_CRED_DISPONIBLE15_1() {
        return LINEA_CRED_DISPONIBLE15_1;
    }

    public void setLINEA_CRED_DISPONIBLE15_1(BigDecimal LINEA_CRED_DISPONIBLE15_1) {
        this.LINEA_CRED_DISPONIBLE15_1 = LINEA_CRED_DISPONIBLE15_1;
    }

    public BigDecimal getLINEA_CRED_DISPONIBLE15_2() {
        return LINEA_CRED_DISPONIBLE15_2;
    }

    public void setLINEA_CRED_DISPONIBLE15_2(BigDecimal LINEA_CRED_DISPONIBLE15_2) {
        this.LINEA_CRED_DISPONIBLE15_2 = LINEA_CRED_DISPONIBLE15_2;
    }

    public BigDecimal getLINEA_CRED_DISPONIBLE15_3() {
        return LINEA_CRED_DISPONIBLE15_3;
    }

    public void setLINEA_CRED_DISPONIBLE15_3(BigDecimal LINEA_CRED_DISPONIBLE15_3) {
        this.LINEA_CRED_DISPONIBLE15_3 = LINEA_CRED_DISPONIBLE15_3;
    }

    public BigDecimal getLINEA_CRED_DISPONIBLE15_4() {
        return LINEA_CRED_DISPONIBLE15_4;
    }

    public void setLINEA_CRED_DISPONIBLE15_4(BigDecimal LINEA_CRED_DISPONIBLE15_4) {
        this.LINEA_CRED_DISPONIBLE15_4 = LINEA_CRED_DISPONIBLE15_4;
    }

    public BigDecimal getMTO_DEUDA_COM_VIG_MDAEXT16_1() {
        return MTO_DEUDA_COM_VIG_MDAEXT16_1;
    }

    public void setMTO_DEUDA_COM_VIG_MDAEXT16_1(BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_1) {
        this.MTO_DEUDA_COM_VIG_MDAEXT16_1 = MTO_DEUDA_COM_VIG_MDAEXT16_1;
    }

    public BigDecimal getMTO_DEUDA_COM_VIG_MDAEXT16_2() {
        return MTO_DEUDA_COM_VIG_MDAEXT16_2;
    }

    public void setMTO_DEUDA_COM_VIG_MDAEXT16_2(BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_2) {
        this.MTO_DEUDA_COM_VIG_MDAEXT16_2 = MTO_DEUDA_COM_VIG_MDAEXT16_2;
    }

    public BigDecimal getMTO_DEUDA_COM_VIG_MDAEXT16_3() {
        return MTO_DEUDA_COM_VIG_MDAEXT16_3;
    }

    public void setMTO_DEUDA_COM_VIG_MDAEXT16_3(BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_3) {
        this.MTO_DEUDA_COM_VIG_MDAEXT16_3 = MTO_DEUDA_COM_VIG_MDAEXT16_3;
    }

    public BigDecimal getMTO_DEUDA_COM_VIG_MDAEXT16_4() {
        return MTO_DEUDA_COM_VIG_MDAEXT16_4;
    }

    public void setMTO_DEUDA_COM_VIG_MDAEXT16_4(BigDecimal MTO_DEUDA_COM_VIG_MDAEXT16_4) {
        this.MTO_DEUDA_COM_VIG_MDAEXT16_4 = MTO_DEUDA_COM_VIG_MDAEXT16_4;
    }

    public BigDecimal getMTO_DEUDA_COM_VENCI_MEXT17_1() {
        return MTO_DEUDA_COM_VENCI_MEXT17_1;
    }

    public void setMTO_DEUDA_COM_VENCI_MEXT17_1(BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_1) {
        this.MTO_DEUDA_COM_VENCI_MEXT17_1 = MTO_DEUDA_COM_VENCI_MEXT17_1;
    }

    public BigDecimal getMTO_DEUDA_COM_VENCI_MEXT17_2() {
        return MTO_DEUDA_COM_VENCI_MEXT17_2;
    }

    public void setMTO_DEUDA_COM_VENCI_MEXT17_2(BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_2) {
        this.MTO_DEUDA_COM_VENCI_MEXT17_2 = MTO_DEUDA_COM_VENCI_MEXT17_2;
    }

    public BigDecimal getMTO_DEUDA_COM_VENCI_MEXT17_3() {
        return MTO_DEUDA_COM_VENCI_MEXT17_3;
    }

    public void setMTO_DEUDA_COM_VENCI_MEXT17_3(BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_3) {
        this.MTO_DEUDA_COM_VENCI_MEXT17_3 = MTO_DEUDA_COM_VENCI_MEXT17_3;
    }

    public BigDecimal getMTO_DEUDA_COM_VENCI_MEXT17_4() {
        return MTO_DEUDA_COM_VENCI_MEXT17_4;
    }

    public void setMTO_DEUDA_COM_VENCI_MEXT17_4(BigDecimal MTO_DEUDA_COM_VENCI_MEXT17_4) {
        this.MTO_DEUDA_COM_VENCI_MEXT17_4 = MTO_DEUDA_COM_VENCI_MEXT17_4;
    }

    public BigDecimal getNRO_INST_CRED_COM18_1() {
        return NRO_INST_CRED_COM18_1;
    }

    public void setNRO_INST_CRED_COM18_1(BigDecimal NRO_INST_CRED_COM18_1) {
        this.NRO_INST_CRED_COM18_1 = NRO_INST_CRED_COM18_1;
    }

    public BigDecimal getNRO_INST_CRED_COM18_2() {
        return NRO_INST_CRED_COM18_2;
    }

    public void setNRO_INST_CRED_COM18_2(BigDecimal NRO_INST_CRED_COM18_2) {
        this.NRO_INST_CRED_COM18_2 = NRO_INST_CRED_COM18_2;
    }

    public BigDecimal getNRO_INST_CRED_COM18_3() {
        return NRO_INST_CRED_COM18_3;
    }

    public void setNRO_INST_CRED_COM18_3(BigDecimal NRO_INST_CRED_COM18_3) {
        this.NRO_INST_CRED_COM18_3 = NRO_INST_CRED_COM18_3;
    }

    public BigDecimal getNRO_INST_CRED_COM18_4() {
        return NRO_INST_CRED_COM18_4;
    }

    public void setNRO_INST_CRED_COM18_4(BigDecimal NRO_INST_CRED_COM18_4) {
        this.NRO_INST_CRED_COM18_4 = NRO_INST_CRED_COM18_4;
    }

    public BigDecimal getDEUDA_LEASING19_1() {
        return DEUDA_LEASING19_1;
    }

    public void setDEUDA_LEASING19_1(BigDecimal DEUDA_LEASING19_1) {
        this.DEUDA_LEASING19_1 = DEUDA_LEASING19_1;
    }

    public BigDecimal getDEUDA_LEASING19_2() {
        return DEUDA_LEASING19_2;
    }

    public void setDEUDA_LEASING19_2(BigDecimal DEUDA_LEASING19_2) {
        this.DEUDA_LEASING19_2 = DEUDA_LEASING19_2;
    }

    public BigDecimal getDEUDA_LEASING19_3() {
        return DEUDA_LEASING19_3;
    }

    public void setDEUDA_LEASING19_3(BigDecimal DEUDA_LEASING19_3) {
        this.DEUDA_LEASING19_3 = DEUDA_LEASING19_3;
    }

    public BigDecimal getDEUDA_LEASING19_4() {
        return DEUDA_LEASING19_4;
    }

    public void setDEUDA_LEASING19_4(BigDecimal DEUDA_LEASING19_4) {
        this.DEUDA_LEASING19_4 = DEUDA_LEASING19_4;
    }

    public BigDecimal getDEUDA_MOROSA_LEASING20_1() {
        return DEUDA_MOROSA_LEASING20_1;
    }

    public void setDEUDA_MOROSA_LEASING20_1(BigDecimal DEUDA_MOROSA_LEASING20_1) {
        this.DEUDA_MOROSA_LEASING20_1 = DEUDA_MOROSA_LEASING20_1;
    }

    public BigDecimal getDEUDA_MOROSA_LEASING20_2() {
        return DEUDA_MOROSA_LEASING20_2;
    }

    public void setDEUDA_MOROSA_LEASING20_2(BigDecimal DEUDA_MOROSA_LEASING20_2) {
        this.DEUDA_MOROSA_LEASING20_2 = DEUDA_MOROSA_LEASING20_2;
    }

    public BigDecimal getDEUDA_MOROSA_LEASING20_3() {
        return DEUDA_MOROSA_LEASING20_3;
    }

    public void setDEUDA_MOROSA_LEASING20_3(BigDecimal DEUDA_MOROSA_LEASING20_3) {
        this.DEUDA_MOROSA_LEASING20_3 = DEUDA_MOROSA_LEASING20_3;
    }

    public BigDecimal getDEUDA_MOROSA_LEASING20_4() {
        return DEUDA_MOROSA_LEASING20_4;
    }

    public void setDEUDA_MOROSA_LEASING20_4(BigDecimal DEUDA_MOROSA_LEASING20_4) {
        this.DEUDA_MOROSA_LEASING20_4 = DEUDA_MOROSA_LEASING20_4;
    }

    public BigDecimal getDEUDA_CRED_CONTINGENTES21_1() {
        return DEUDA_CRED_CONTINGENTES21_1;
    }

    public void setDEUDA_CRED_CONTINGENTES21_1(BigDecimal DEUDA_CRED_CONTINGENTES21_1) {
        this.DEUDA_CRED_CONTINGENTES21_1 = DEUDA_CRED_CONTINGENTES21_1;
    }

    public BigDecimal getDEUDA_CRED_CONTINGENTES21_2() {
        return DEUDA_CRED_CONTINGENTES21_2;
    }

    public void setDEUDA_CRED_CONTINGENTES21_2(BigDecimal DEUDA_CRED_CONTINGENTES21_2) {
        this.DEUDA_CRED_CONTINGENTES21_2 = DEUDA_CRED_CONTINGENTES21_2;
    }

    public BigDecimal getDEUDA_CRED_CONTINGENTES21_3() {
        return DEUDA_CRED_CONTINGENTES21_3;
    }

    public void setDEUDA_CRED_CONTINGENTES21_3(BigDecimal DEUDA_CRED_CONTINGENTES21_3) {
        this.DEUDA_CRED_CONTINGENTES21_3 = DEUDA_CRED_CONTINGENTES21_3;
    }

    public BigDecimal getDEUDA_CRED_CONTINGENTES21_4() {
        return DEUDA_CRED_CONTINGENTES21_4;
    }

    public void setDEUDA_CRED_CONTINGENTES21_4(BigDecimal DEUDA_CRED_CONTINGENTES21_4) {
        this.DEUDA_CRED_CONTINGENTES21_4 = DEUDA_CRED_CONTINGENTES21_4;
    }

    public BigDecimal getDEUDA_DIR_90_18022_1() {
        return DEUDA_DIR_90_18022_1;
    }

    public void setDEUDA_DIR_90_18022_1(BigDecimal DEUDA_DIR_90_18022_1) {
        this.DEUDA_DIR_90_18022_1 = DEUDA_DIR_90_18022_1;
    }

    public BigDecimal getDEUDA_DIR_90_18022_2() {
        return DEUDA_DIR_90_18022_2;
    }

    public void setDEUDA_DIR_90_18022_2(BigDecimal DEUDA_DIR_90_18022_2) {
        this.DEUDA_DIR_90_18022_2 = DEUDA_DIR_90_18022_2;
    }

    public BigDecimal getDEUDA_DIR_90_18022_3() {
        return DEUDA_DIR_90_18022_3;
    }

    public void setDEUDA_DIR_90_18022_3(BigDecimal DEUDA_DIR_90_18022_3) {
        this.DEUDA_DIR_90_18022_3 = DEUDA_DIR_90_18022_3;
    }

    public BigDecimal getDEUDA_DIR_90_18022_4() {
        return DEUDA_DIR_90_18022_4;
    }

    public void setDEUDA_DIR_90_18022_4(BigDecimal DEUDA_DIR_90_18022_4) {
        this.DEUDA_DIR_90_18022_4 = DEUDA_DIR_90_18022_4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ObtieneDatosDeudaSbif that = (ObtieneDatosDeudaSbif) o;
        return Objects.equals(GLSDEUDA_01, that.GLSDEUDA_01) && Objects.equals(GLSDEUDA_02, that.GLSDEUDA_02) && Objects.equals(GLSDEUDA_03, that.GLSDEUDA_03) && Objects
            .equals(GLSDEUDA_04, that.GLSDEUDA_04) && Objects.equals(GLSDEUDA_05, that.GLSDEUDA_05) && Objects.equals(GLSDEUDA_06, that.GLSDEUDA_06) && Objects
            .equals(GLSDEUDA_07, that.GLSDEUDA_07) && Objects.equals(GLSDEUDA_08, that.GLSDEUDA_08) && Objects.equals(GLSDEUDA_09, that.GLSDEUDA_09) && Objects
            .equals(GLSDEUDA_10, that.GLSDEUDA_10) && Objects.equals(GLSDEUDA_11, that.GLSDEUDA_11) && Objects.equals(GLSDEUDA_12, that.GLSDEUDA_12) && Objects
            .equals(GLSDEUDA_13, that.GLSDEUDA_13) && Objects.equals(GLSDEUDA_14, that.GLSDEUDA_14) && Objects.equals(GLSDEUDA_15, that.GLSDEUDA_15) && Objects
            .equals(GLSDEUDA_16, that.GLSDEUDA_16) && Objects.equals(GLSDEUDA_17, that.GLSDEUDA_17) && Objects.equals(GLSDEUDA_18, that.GLSDEUDA_18) && Objects
            .equals(GLSDEUDA_19, that.GLSDEUDA_19) && Objects.equals(GLSDEUDA_21, that.GLSDEUDA_21) && Objects.equals(GLSDEUDA_22, that.GLSDEUDA_22) && Objects
            .equals(PERIODO01_1, that.PERIODO01_1) && Objects.equals(PERIODO01_2, that.PERIODO01_2) && Objects.equals(PERIODO01_3, that.PERIODO01_3) && Objects
            .equals(PERIODO01_4, that.PERIODO01_4) && Objects.equals(DEUDA_DIREC_VIG02_1, that.DEUDA_DIREC_VIG02_1) && Objects.equals(DEUDA_DIREC_VIG02_2, that.DEUDA_DIREC_VIG02_2)
            && Objects.equals(DEUDA_DIREC_VIG02_3, that.DEUDA_DIREC_VIG02_3) && Objects.equals(DEUDA_DIREC_VIG02_4, that.DEUDA_DIREC_VIG02_4) && Objects
            .equals(DEUDA_X_INV_FINAN03_1, that.DEUDA_X_INV_FINAN03_1) && Objects.equals(DEUDA_X_INV_FINAN03_2, that.DEUDA_X_INV_FINAN03_2) && Objects
            .equals(DEUDA_X_INV_FINAN03_3, that.DEUDA_X_INV_FINAN03_3) && Objects.equals(DEUDA_X_INV_FINAN03_4, that.DEUDA_X_INV_FINAN03_4) && Objects
            .equals(DEUDA_VENCI_DIREC04_1, that.DEUDA_VENCI_DIREC04_1) && Objects.equals(DEUDA_VENCI_DIREC04_2, that.DEUDA_VENCI_DIREC04_2) && Objects
            .equals(DEUDA_VENCI_DIREC04_3, that.DEUDA_VENCI_DIREC04_3) && Objects.equals(DEUDA_VENCI_DIREC04_4, that.DEUDA_VENCI_DIREC04_4) && Objects
            .equals(DEUDA_X_OPE_C_PACTO05_1, that.DEUDA_X_OPE_C_PACTO05_1) && Objects.equals(DEUDA_X_OPE_C_PACTO05_2, that.DEUDA_X_OPE_C_PACTO05_2) && Objects
            .equals(DEUDA_X_OPE_C_PACTO05_3, that.DEUDA_X_OPE_C_PACTO05_3) && Objects.equals(DEUDA_X_OPE_C_PACTO05_4, that.DEUDA_X_OPE_C_PACTO05_4) && Objects
            .equals(DEUDA_INDIREC_VIG06_1, that.DEUDA_INDIREC_VIG06_1) && Objects.equals(DEUDA_INDIREC_VIG06_2, that.DEUDA_INDIREC_VIG06_2) && Objects
            .equals(DEUDA_INDIREC_VIG06_3, that.DEUDA_INDIREC_VIG06_3) && Objects.equals(DEUDA_INDIREC_VIG06_4, that.DEUDA_INDIREC_VIG06_4) && Objects
            .equals(DEUDA_INDIREC_VENCI07_1, that.DEUDA_INDIREC_VENCI07_1) && Objects.equals(DEUDA_INDIREC_VENCI07_2, that.DEUDA_INDIREC_VENCI07_2) && Objects
            .equals(DEUDA_INDIREC_VENCI07_3, that.DEUDA_INDIREC_VENCI07_3) && Objects.equals(DEUDA_INDIREC_VENCI07_4, that.DEUDA_INDIREC_VENCI07_4) && Objects
            .equals(DEUDA_COMERCIAL08_1, that.DEUDA_COMERCIAL08_1) && Objects.equals(DEUDA_COMERCIAL08_2, that.DEUDA_COMERCIAL08_2) && Objects
            .equals(DEUDA_COMERCIAL08_3, that.DEUDA_COMERCIAL08_3) && Objects.equals(DEUDA_COMERCIAL08_4, that.DEUDA_COMERCIAL08_4) && Objects
            .equals(DEUDA_X_CRED_CONSUMO09_1, that.DEUDA_X_CRED_CONSUMO09_1) && Objects.equals(DEUDA_X_CRED_CONSUMO09_2, that.DEUDA_X_CRED_CONSUMO09_2) && Objects
            .equals(DEUDA_X_CRED_CONSUMO09_3, that.DEUDA_X_CRED_CONSUMO09_3) && Objects.equals(DEUDA_X_CRED_CONSUMO09_4, that.DEUDA_X_CRED_CONSUMO09_4) && Objects
            .equals(NRO_ACREE_X_CRED_CONSUMO10_1, that.NRO_ACREE_X_CRED_CONSUMO10_1) && Objects.equals(NRO_ACREE_X_CRED_CONSUMO10_2, that.NRO_ACREE_X_CRED_CONSUMO10_2) && Objects
            .equals(NRO_ACREE_X_CRED_CONSUMO10_3, that.NRO_ACREE_X_CRED_CONSUMO10_3) && Objects.equals(NRO_ACREE_X_CRED_CONSUMO10_4, that.NRO_ACREE_X_CRED_CONSUMO10_4) && Objects
            .equals(DEUDA_X_CRED_HIPOTECA11_1, that.DEUDA_X_CRED_HIPOTECA11_1) && Objects.equals(DEUDA_X_CRED_HIPOTECA11_2, that.DEUDA_X_CRED_HIPOTECA11_2) && Objects
            .equals(DEUDA_X_CRED_HIPOTECA11_3, that.DEUDA_X_CRED_HIPOTECA11_3) && Objects.equals(DEUDA_X_CRED_HIPOTECA11_4, that.DEUDA_X_CRED_HIPOTECA11_4) && Objects
            .equals(DEUDA_MOROSA12_1, that.DEUDA_MOROSA12_1) && Objects.equals(DEUDA_MOROSA12_2, that.DEUDA_MOROSA12_2) && Objects.equals(DEUDA_MOROSA12_3, that.DEUDA_MOROSA12_3)
            && Objects.equals(DEUDA_MOROSA12_4, that.DEUDA_MOROSA12_4) && Objects.equals(DEUDA_CAST_DIRECTA13_1, that.DEUDA_CAST_DIRECTA13_1) && Objects
            .equals(DEUDA_CAST_DIRECTA13_2, that.DEUDA_CAST_DIRECTA13_2) && Objects.equals(DEUDA_CAST_DIRECTA13_3, that.DEUDA_CAST_DIRECTA13_3) && Objects
            .equals(DEUDA_CAST_DIRECTA13_4, that.DEUDA_CAST_DIRECTA13_4) && Objects.equals(DEUDA_CAST_INDIRECTA14_1, that.DEUDA_CAST_INDIRECTA14_1) && Objects
            .equals(DEUDA_CAST_INDIRECTA14_2, that.DEUDA_CAST_INDIRECTA14_2) && Objects.equals(DEUDA_CAST_INDIRECTA14_3, that.DEUDA_CAST_INDIRECTA14_3) && Objects
            .equals(DEUDA_CAST_INDIRECTA14_4, that.DEUDA_CAST_INDIRECTA14_4) && Objects.equals(LINEA_CRED_DISPONIBLE15_1, that.LINEA_CRED_DISPONIBLE15_1) && Objects
            .equals(LINEA_CRED_DISPONIBLE15_2, that.LINEA_CRED_DISPONIBLE15_2) && Objects.equals(LINEA_CRED_DISPONIBLE15_3, that.LINEA_CRED_DISPONIBLE15_3) && Objects
            .equals(LINEA_CRED_DISPONIBLE15_4, that.LINEA_CRED_DISPONIBLE15_4) && Objects.equals(MTO_DEUDA_COM_VIG_MDAEXT16_1, that.MTO_DEUDA_COM_VIG_MDAEXT16_1) && Objects
            .equals(MTO_DEUDA_COM_VIG_MDAEXT16_2, that.MTO_DEUDA_COM_VIG_MDAEXT16_2) && Objects.equals(MTO_DEUDA_COM_VIG_MDAEXT16_3, that.MTO_DEUDA_COM_VIG_MDAEXT16_3) && Objects
            .equals(MTO_DEUDA_COM_VIG_MDAEXT16_4, that.MTO_DEUDA_COM_VIG_MDAEXT16_4) && Objects.equals(MTO_DEUDA_COM_VENCI_MEXT17_1, that.MTO_DEUDA_COM_VENCI_MEXT17_1) && Objects
            .equals(MTO_DEUDA_COM_VENCI_MEXT17_2, that.MTO_DEUDA_COM_VENCI_MEXT17_2) && Objects.equals(MTO_DEUDA_COM_VENCI_MEXT17_3, that.MTO_DEUDA_COM_VENCI_MEXT17_3) && Objects
            .equals(MTO_DEUDA_COM_VENCI_MEXT17_4, that.MTO_DEUDA_COM_VENCI_MEXT17_4) && Objects.equals(NRO_INST_CRED_COM18_1, that.NRO_INST_CRED_COM18_1) && Objects
            .equals(NRO_INST_CRED_COM18_2, that.NRO_INST_CRED_COM18_2) && Objects.equals(NRO_INST_CRED_COM18_3, that.NRO_INST_CRED_COM18_3) && Objects
            .equals(NRO_INST_CRED_COM18_4, that.NRO_INST_CRED_COM18_4) && Objects.equals(DEUDA_LEASING19_1, that.DEUDA_LEASING19_1) && Objects
            .equals(DEUDA_LEASING19_2, that.DEUDA_LEASING19_2) && Objects.equals(DEUDA_LEASING19_3, that.DEUDA_LEASING19_3) && Objects
            .equals(DEUDA_LEASING19_4, that.DEUDA_LEASING19_4) && Objects.equals(DEUDA_MOROSA_LEASING20_1, that.DEUDA_MOROSA_LEASING20_1) && Objects
            .equals(DEUDA_MOROSA_LEASING20_2, that.DEUDA_MOROSA_LEASING20_2) && Objects.equals(DEUDA_MOROSA_LEASING20_3, that.DEUDA_MOROSA_LEASING20_3) && Objects
            .equals(DEUDA_MOROSA_LEASING20_4, that.DEUDA_MOROSA_LEASING20_4) && Objects.equals(DEUDA_CRED_CONTINGENTES21_1, that.DEUDA_CRED_CONTINGENTES21_1) && Objects
            .equals(DEUDA_CRED_CONTINGENTES21_2, that.DEUDA_CRED_CONTINGENTES21_2) && Objects.equals(DEUDA_CRED_CONTINGENTES21_3, that.DEUDA_CRED_CONTINGENTES21_3) && Objects
            .equals(DEUDA_CRED_CONTINGENTES21_4, that.DEUDA_CRED_CONTINGENTES21_4) && Objects.equals(DEUDA_DIR_90_18022_1, that.DEUDA_DIR_90_18022_1) && Objects
            .equals(DEUDA_DIR_90_18022_2, that.DEUDA_DIR_90_18022_2) && Objects.equals(DEUDA_DIR_90_18022_3, that.DEUDA_DIR_90_18022_3) && Objects
            .equals(DEUDA_DIR_90_18022_4, that.DEUDA_DIR_90_18022_4);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(GLSDEUDA_01, GLSDEUDA_02, GLSDEUDA_03, GLSDEUDA_04, GLSDEUDA_05, GLSDEUDA_06, GLSDEUDA_07, GLSDEUDA_08, GLSDEUDA_09, GLSDEUDA_10, GLSDEUDA_11, GLSDEUDA_12,
                GLSDEUDA_13, GLSDEUDA_14, GLSDEUDA_15, GLSDEUDA_16, GLSDEUDA_17, GLSDEUDA_18, GLSDEUDA_19, GLSDEUDA_21, GLSDEUDA_22, PERIODO01_1, PERIODO01_2, PERIODO01_3,
                PERIODO01_4, DEUDA_DIREC_VIG02_1, DEUDA_DIREC_VIG02_2, DEUDA_DIREC_VIG02_3, DEUDA_DIREC_VIG02_4, DEUDA_X_INV_FINAN03_1, DEUDA_X_INV_FINAN03_2,
                DEUDA_X_INV_FINAN03_3, DEUDA_X_INV_FINAN03_4, DEUDA_VENCI_DIREC04_1, DEUDA_VENCI_DIREC04_2, DEUDA_VENCI_DIREC04_3, DEUDA_VENCI_DIREC04_4, DEUDA_X_OPE_C_PACTO05_1,
                DEUDA_X_OPE_C_PACTO05_2, DEUDA_X_OPE_C_PACTO05_3, DEUDA_X_OPE_C_PACTO05_4, DEUDA_INDIREC_VIG06_1, DEUDA_INDIREC_VIG06_2, DEUDA_INDIREC_VIG06_3,
                DEUDA_INDIREC_VIG06_4, DEUDA_INDIREC_VENCI07_1, DEUDA_INDIREC_VENCI07_2, DEUDA_INDIREC_VENCI07_3, DEUDA_INDIREC_VENCI07_4, DEUDA_COMERCIAL08_1, DEUDA_COMERCIAL08_2,
                DEUDA_COMERCIAL08_3, DEUDA_COMERCIAL08_4, DEUDA_X_CRED_CONSUMO09_1, DEUDA_X_CRED_CONSUMO09_2, DEUDA_X_CRED_CONSUMO09_3, DEUDA_X_CRED_CONSUMO09_4,
                NRO_ACREE_X_CRED_CONSUMO10_1, NRO_ACREE_X_CRED_CONSUMO10_2, NRO_ACREE_X_CRED_CONSUMO10_3, NRO_ACREE_X_CRED_CONSUMO10_4, DEUDA_X_CRED_HIPOTECA11_1,
                DEUDA_X_CRED_HIPOTECA11_2, DEUDA_X_CRED_HIPOTECA11_3, DEUDA_X_CRED_HIPOTECA11_4, DEUDA_MOROSA12_1, DEUDA_MOROSA12_2, DEUDA_MOROSA12_3, DEUDA_MOROSA12_4,
                DEUDA_CAST_DIRECTA13_1, DEUDA_CAST_DIRECTA13_2, DEUDA_CAST_DIRECTA13_3, DEUDA_CAST_DIRECTA13_4, DEUDA_CAST_INDIRECTA14_1, DEUDA_CAST_INDIRECTA14_2,
                DEUDA_CAST_INDIRECTA14_3, DEUDA_CAST_INDIRECTA14_4, LINEA_CRED_DISPONIBLE15_1, LINEA_CRED_DISPONIBLE15_2, LINEA_CRED_DISPONIBLE15_3, LINEA_CRED_DISPONIBLE15_4,
                MTO_DEUDA_COM_VIG_MDAEXT16_1, MTO_DEUDA_COM_VIG_MDAEXT16_2, MTO_DEUDA_COM_VIG_MDAEXT16_3, MTO_DEUDA_COM_VIG_MDAEXT16_4, MTO_DEUDA_COM_VENCI_MEXT17_1,
                MTO_DEUDA_COM_VENCI_MEXT17_2, MTO_DEUDA_COM_VENCI_MEXT17_3, MTO_DEUDA_COM_VENCI_MEXT17_4, NRO_INST_CRED_COM18_1, NRO_INST_CRED_COM18_2, NRO_INST_CRED_COM18_3,
                NRO_INST_CRED_COM18_4, DEUDA_LEASING19_1, DEUDA_LEASING19_2, DEUDA_LEASING19_3, DEUDA_LEASING19_4, DEUDA_MOROSA_LEASING20_1, DEUDA_MOROSA_LEASING20_2,
                DEUDA_MOROSA_LEASING20_3, DEUDA_MOROSA_LEASING20_4, DEUDA_CRED_CONTINGENTES21_1, DEUDA_CRED_CONTINGENTES21_2, DEUDA_CRED_CONTINGENTES21_3,
                DEUDA_CRED_CONTINGENTES21_4, DEUDA_DIR_90_18022_1, DEUDA_DIR_90_18022_2, DEUDA_DIR_90_18022_3, DEUDA_DIR_90_18022_4);
    }

    @Override
    public String toString() {
        return "ObtieneDatosDeudaSbif{" + "GLSDEUDA_01='" + GLSDEUDA_01 + '\'' + ", GLSDEUDA_02='" + GLSDEUDA_02 + '\'' + ", GLSDEUDA_03='" + GLSDEUDA_03 + '\'' + ", GLSDEUDA_04='"
            + GLSDEUDA_04 + '\'' + ", GLSDEUDA_05='" + GLSDEUDA_05 + '\'' + ", GLSDEUDA_06='" + GLSDEUDA_06 + '\'' + ", GLSDEUDA_07='" + GLSDEUDA_07 + '\'' + ", GLSDEUDA_08='"
            + GLSDEUDA_08 + '\'' + ", GLSDEUDA_09='" + GLSDEUDA_09 + '\'' + ", GLSDEUDA_10='" + GLSDEUDA_10 + '\'' + ", GLSDEUDA_11='" + GLSDEUDA_11 + '\'' + ", GLSDEUDA_12='"
            + GLSDEUDA_12 + '\'' + ", GLSDEUDA_13='" + GLSDEUDA_13 + '\'' + ", GLSDEUDA_14='" + GLSDEUDA_14 + '\'' + ", GLSDEUDA_15='" + GLSDEUDA_15 + '\'' + ", GLSDEUDA_16='"
            + GLSDEUDA_16 + '\'' + ", GLSDEUDA_17='" + GLSDEUDA_17 + '\'' + ", GLSDEUDA_18='" + GLSDEUDA_18 + '\'' + ", GLSDEUDA_19='" + GLSDEUDA_19 + '\'' + ", GLSDEUDA_21='"
            + GLSDEUDA_21 + '\'' + ", GLSDEUDA_22='" + GLSDEUDA_22 + '\'' + ", PERIODO01_1=" + PERIODO01_1 + ", PERIODO01_2=" + PERIODO01_2 + ", PERIODO01_3=" + PERIODO01_3
            + ", PERIODO01_4=" + PERIODO01_4 + ", DEUDA_DIREC_VIG02_1=" + DEUDA_DIREC_VIG02_1 + ", DEUDA_DIREC_VIG02_2=" + DEUDA_DIREC_VIG02_2 + ", DEUDA_DIREC_VIG02_3="
            + DEUDA_DIREC_VIG02_3 + ", DEUDA_DIREC_VIG02_4=" + DEUDA_DIREC_VIG02_4 + ", DEUDA_X_INV_FINAN03_1=" + DEUDA_X_INV_FINAN03_1 + ", DEUDA_X_INV_FINAN03_2="
            + DEUDA_X_INV_FINAN03_2 + ", DEUDA_X_INV_FINAN03_3=" + DEUDA_X_INV_FINAN03_3 + ", DEUDA_X_INV_FINAN03_4=" + DEUDA_X_INV_FINAN03_4 + ", DEUDA_VENCI_DIREC04_1="
            + DEUDA_VENCI_DIREC04_1 + ", DEUDA_VENCI_DIREC04_2=" + DEUDA_VENCI_DIREC04_2 + ", DEUDA_VENCI_DIREC04_3=" + DEUDA_VENCI_DIREC04_3 + ", DEUDA_VENCI_DIREC04_4="
            + DEUDA_VENCI_DIREC04_4 + ", DEUDA_X_OPE_C_PACTO05_1=" + DEUDA_X_OPE_C_PACTO05_1 + ", DEUDA_X_OPE_C_PACTO05_2=" + DEUDA_X_OPE_C_PACTO05_2 + ", DEUDA_X_OPE_C_PACTO05_3="
            + DEUDA_X_OPE_C_PACTO05_3 + ", DEUDA_X_OPE_C_PACTO05_4=" + DEUDA_X_OPE_C_PACTO05_4 + ", DEUDA_INDIREC_VIG06_1=" + DEUDA_INDIREC_VIG06_1 + ", DEUDA_INDIREC_VIG06_2="
            + DEUDA_INDIREC_VIG06_2 + ", DEUDA_INDIREC_VIG06_3=" + DEUDA_INDIREC_VIG06_3 + ", DEUDA_INDIREC_VIG06_4=" + DEUDA_INDIREC_VIG06_4 + ", DEUDA_INDIREC_VENCI07_1="
            + DEUDA_INDIREC_VENCI07_1 + ", DEUDA_INDIREC_VENCI07_2=" + DEUDA_INDIREC_VENCI07_2 + ", DEUDA_INDIREC_VENCI07_3=" + DEUDA_INDIREC_VENCI07_3
            + ", DEUDA_INDIREC_VENCI07_4=" + DEUDA_INDIREC_VENCI07_4 + ", DEUDA_COMERCIAL08_1=" + DEUDA_COMERCIAL08_1 + ", DEUDA_COMERCIAL08_2=" + DEUDA_COMERCIAL08_2
            + ", DEUDA_COMERCIAL08_3=" + DEUDA_COMERCIAL08_3 + ", DEUDA_COMERCIAL08_4=" + DEUDA_COMERCIAL08_4 + ", DEUDA_X_CRED_CONSUMO09_1=" + DEUDA_X_CRED_CONSUMO09_1
            + ", DEUDA_X_CRED_CONSUMO09_2=" + DEUDA_X_CRED_CONSUMO09_2 + ", DEUDA_X_CRED_CONSUMO09_3=" + DEUDA_X_CRED_CONSUMO09_3 + ", DEUDA_X_CRED_CONSUMO09_4="
            + DEUDA_X_CRED_CONSUMO09_4 + ", NRO_ACREE_X_CRED_CONSUMO10_1=" + NRO_ACREE_X_CRED_CONSUMO10_1 + ", NRO_ACREE_X_CRED_CONSUMO10_2=" + NRO_ACREE_X_CRED_CONSUMO10_2
            + ", NRO_ACREE_X_CRED_CONSUMO10_3=" + NRO_ACREE_X_CRED_CONSUMO10_3 + ", NRO_ACREE_X_CRED_CONSUMO10_4=" + NRO_ACREE_X_CRED_CONSUMO10_4 + ", DEUDA_X_CRED_HIPOTECA11_1="
            + DEUDA_X_CRED_HIPOTECA11_1 + ", DEUDA_X_CRED_HIPOTECA11_2=" + DEUDA_X_CRED_HIPOTECA11_2 + ", DEUDA_X_CRED_HIPOTECA11_3=" + DEUDA_X_CRED_HIPOTECA11_3
            + ", DEUDA_X_CRED_HIPOTECA11_4=" + DEUDA_X_CRED_HIPOTECA11_4 + ", DEUDA_MOROSA12_1=" + DEUDA_MOROSA12_1 + ", DEUDA_MOROSA12_2=" + DEUDA_MOROSA12_2
            + ", DEUDA_MOROSA12_3=" + DEUDA_MOROSA12_3 + ", DEUDA_MOROSA12_4=" + DEUDA_MOROSA12_4 + ", DEUDA_CAST_DIRECTA13_1=" + DEUDA_CAST_DIRECTA13_1
            + ", DEUDA_CAST_DIRECTA13_2=" + DEUDA_CAST_DIRECTA13_2 + ", DEUDA_CAST_DIRECTA13_3=" + DEUDA_CAST_DIRECTA13_3 + ", DEUDA_CAST_DIRECTA13_4=" + DEUDA_CAST_DIRECTA13_4
            + ", DEUDA_CAST_INDIRECTA14_1=" + DEUDA_CAST_INDIRECTA14_1 + ", DEUDA_CAST_INDIRECTA14_2=" + DEUDA_CAST_INDIRECTA14_2 + ", DEUDA_CAST_INDIRECTA14_3="
            + DEUDA_CAST_INDIRECTA14_3 + ", DEUDA_CAST_INDIRECTA14_4=" + DEUDA_CAST_INDIRECTA14_4 + ", LINEA_CRED_DISPONIBLE15_1=" + LINEA_CRED_DISPONIBLE15_1
            + ", LINEA_CRED_DISPONIBLE15_2=" + LINEA_CRED_DISPONIBLE15_2 + ", LINEA_CRED_DISPONIBLE15_3=" + LINEA_CRED_DISPONIBLE15_3 + ", LINEA_CRED_DISPONIBLE15_4="
            + LINEA_CRED_DISPONIBLE15_4 + ", MTO_DEUDA_COM_VIG_MDAEXT16_1=" + MTO_DEUDA_COM_VIG_MDAEXT16_1 + ", MTO_DEUDA_COM_VIG_MDAEXT16_2=" + MTO_DEUDA_COM_VIG_MDAEXT16_2
            + ", MTO_DEUDA_COM_VIG_MDAEXT16_3=" + MTO_DEUDA_COM_VIG_MDAEXT16_3 + ", MTO_DEUDA_COM_VIG_MDAEXT16_4=" + MTO_DEUDA_COM_VIG_MDAEXT16_4
            + ", MTO_DEUDA_COM_VENCI_MEXT17_1=" + MTO_DEUDA_COM_VENCI_MEXT17_1 + ", MTO_DEUDA_COM_VENCI_MEXT17_2=" + MTO_DEUDA_COM_VENCI_MEXT17_2
            + ", MTO_DEUDA_COM_VENCI_MEXT17_3=" + MTO_DEUDA_COM_VENCI_MEXT17_3 + ", MTO_DEUDA_COM_VENCI_MEXT17_4=" + MTO_DEUDA_COM_VENCI_MEXT17_4 + ", NRO_INST_CRED_COM18_1="
            + NRO_INST_CRED_COM18_1 + ", NRO_INST_CRED_COM18_2=" + NRO_INST_CRED_COM18_2 + ", NRO_INST_CRED_COM18_3=" + NRO_INST_CRED_COM18_3 + ", NRO_INST_CRED_COM18_4="
            + NRO_INST_CRED_COM18_4 + ", DEUDA_LEASING19_1=" + DEUDA_LEASING19_1 + ", DEUDA_LEASING19_2=" + DEUDA_LEASING19_2 + ", DEUDA_LEASING19_3=" + DEUDA_LEASING19_3
            + ", DEUDA_LEASING19_4=" + DEUDA_LEASING19_4 + ", DEUDA_MOROSA_LEASING20_1=" + DEUDA_MOROSA_LEASING20_1 + ", DEUDA_MOROSA_LEASING20_2=" + DEUDA_MOROSA_LEASING20_2
            + ", DEUDA_MOROSA_LEASING20_3=" + DEUDA_MOROSA_LEASING20_3 + ", DEUDA_MOROSA_LEASING20_4=" + DEUDA_MOROSA_LEASING20_4 + ", DEUDA_CRED_CONTINGENTES21_1="
            + DEUDA_CRED_CONTINGENTES21_1 + ", DEUDA_CRED_CONTINGENTES21_2=" + DEUDA_CRED_CONTINGENTES21_2 + ", DEUDA_CRED_CONTINGENTES21_3=" + DEUDA_CRED_CONTINGENTES21_3
            + ", DEUDA_CRED_CONTINGENTES21_4=" + DEUDA_CRED_CONTINGENTES21_4 + ", DEUDA_DIR_90_18022_1=" + DEUDA_DIR_90_18022_1 + ", DEUDA_DIR_90_18022_2=" + DEUDA_DIR_90_18022_2
            + ", DEUDA_DIR_90_18022_3=" + DEUDA_DIR_90_18022_3 + ", DEUDA_DIR_90_18022_4=" + DEUDA_DIR_90_18022_4 + '}';
    }
}
