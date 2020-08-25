package com.cynoteck.petofyvet.response.addLabWorkResponse;

import com.cynoteck.petofyvet.response.Header;
import com.cynoteck.petofyvet.response.Response;
import com.cynoteck.petofyvet.response.clinicVisist.ClinicVisitsModel;

import java.util.ArrayList;

public class AddLabWorkResponse {

    private AddLabWorkModel data;

    private Response response;

    private Header header;

    public AddLabWorkModel getData ()
    {
        return data;
    }

    public void setData (AddLabWorkModel data)
    {
        this.data = data;
    }

    public Response getResponse ()
    {
        return response;
    }

    public void setResponse (Response response)
    {
        this.response = response;
    }

    public Header getHeader ()
    {
        return header;
    }

    public void setHeader (Header header)
    {
        this.header = header;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", response = "+response+", header = "+header+"]";
    }

}