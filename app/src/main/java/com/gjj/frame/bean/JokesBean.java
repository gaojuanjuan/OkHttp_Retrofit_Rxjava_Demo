package com.gjj.frame.bean;

import java.util.List;

/**
 * Created by gaojuanjuan on 2018/2/28.
 */

public class JokesBean {


    /**
     * reason : Success
     * result : {"data":[{"content":"我高中同学，刚拿出驾照时候，他爹给他买了个奔驰，路上被一个骑电动车的把车刮了，然后骑车妇女开始躺地上撒泼，讹上了。我这同学也是个楞钟，无理还要搅三分的主儿，说：赖上我了是吧？行，有本事你别动。上车打火压过去了\u2026\u2026后来我问他：你轧她干啥？你家这么有钱，给点钱不就得了？他说，轧也是给钱，不轧也是给钱，为啥不轧？","hashId":"aef4cfe74377c11943c99d23d0c6cf45","unixtime":1519793700,"updatetime":"2018-02-28 12:55:00"}]}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * content : 我高中同学，刚拿出驾照时候，他爹给他买了个奔驰，路上被一个骑电动车的把车刮了，然后骑车妇女开始躺地上撒泼，讹上了。我这同学也是个楞钟，无理还要搅三分的主儿，说：赖上我了是吧？行，有本事你别动。上车打火压过去了……后来我问他：你轧她干啥？你家这么有钱，给点钱不就得了？他说，轧也是给钱，不轧也是给钱，为啥不轧？
             * hashId : aef4cfe74377c11943c99d23d0c6cf45
             * unixtime : 1519793700
             * updatetime : 2018-02-28 12:55:00
             */

            private String content;
            private String hashId;
            private int unixtime;
            private String updatetime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHashId() {
                return hashId;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public int getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(int unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }
        }
    }
}
