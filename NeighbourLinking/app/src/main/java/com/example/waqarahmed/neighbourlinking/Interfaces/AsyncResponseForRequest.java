package com.example.waqarahmed.neighbourlinking.Interfaces;

import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.Classes.ServicesTypes;

import java.util.ArrayList;

/**
 * Created by waqar on 6/23/2017.
 */

public interface AsyncResponseForRequest {
    void processFinish(ArrayList<ServiceRequest> Requestlist);
}
