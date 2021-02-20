package com.example.android_retrofit;

import java.util.List;

public class IotaItTestApiList {
    private List<IotaItTestAPI> data;

    public IotaItTestApiList() {}

    public IotaItTestApiList(List<IotaItTestAPI> data) {
        this.data = data;
    }

    public List<IotaItTestAPI> getData() {
        return data;
    }

    public void setData(List<IotaItTestAPI> data) {
        this.data = data;
    }
}
