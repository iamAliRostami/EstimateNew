package com.leon.estimate_new.infrastructure;

import retrofit2.Response;

/**
 * Created by Leon on 12/12/2017.
 */

public interface ICallback<T> {
    void execute(Response<T> response);
}
