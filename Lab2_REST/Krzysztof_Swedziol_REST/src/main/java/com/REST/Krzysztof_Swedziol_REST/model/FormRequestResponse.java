package com.REST.Krzysztof_Swedziol_REST.model;

public class FormRequestResponse {
    private FormsModel form;
    private ResponseModel response;

    public FormRequestResponse() {}

    public FormRequestResponse(FormsModel form, ResponseModel response) {
        this.form = form;
        this.response = response;
    }

    public FormsModel getForm() {
        return form;
    }

    public void setForm(FormsModel form) {
        this.form = form;
    }

    public ResponseModel getResponse() {
        return response;
    }

    public void setResponse(ResponseModel response) {
        this.response = response;
    }
}

