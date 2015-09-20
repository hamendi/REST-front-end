package net.hamendi;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BooleanResponse {

    Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
