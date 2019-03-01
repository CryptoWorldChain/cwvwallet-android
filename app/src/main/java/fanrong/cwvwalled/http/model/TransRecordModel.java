package fanrong.cwvwalled.http.model;

public class TransRecordModel {

    public String address;

    public String count;

    //0完成 1等待中 2失败
    public int statu;

    //1转账 2收款
    public int type;

    public TransRecordModel(String address, int statu, String count, int type) {
        this.address = address;
        this.statu = statu;
        this.count = count;
        this.type = type;
    }

}
