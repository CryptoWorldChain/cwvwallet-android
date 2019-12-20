package fanrong.cwvwalled.litepal;

import android.content.ContentValues;

import org.litepal.LitePal;

import java.util.List;

import fanrong.cwvwalled.http.model.NodeModel;
import fanrong.cwvwalled.litepal.GreNodeModel;
import xianchao.com.basiclib.utils.CheckedUtils;

public class GreNodeOperator {
    public static GreNodeModel copyFromNodeModel(String nodeModel) {

        String realNodeModel = nodeModel;
        if (!nodeModel.startsWith("http")) {
            realNodeModel = "http://" + nodeModel;
        }
        GreNodeModel greNodeModel = new GreNodeModel(realNodeModel);
        return greNodeModel;

    }

    public static void insert(GreNodeModel greNodeModel) {
        List<GreNodeModel> list = LitePal.where("node_url like ? and node_name like ?"
                , greNodeModel.getNode_url()
                , greNodeModel.getNode_name())
                .find(GreNodeModel.class);
        if (CheckedUtils.INSTANCE.isEmpty(list)) {
            greNodeModel.save();
        }
    }

    public static GreNodeModel queryCWVnode() {
        List<GreNodeModel> list = LitePal.where("isUsing like ? and node_name like ?"
                , "1", "CWV")
                .find(GreNodeModel.class);
        if (CheckedUtils.INSTANCE.nonEmpty(list)) {
            return list.get(0);
        }


        List<GreNodeModel> defList = LitePal.where("is_def like ? and node_name like ?"
                , "1", "CWV")
                .find(GreNodeModel.class);
        if (CheckedUtils.INSTANCE.nonEmpty(defList)) {
            return defList.get(0);
        }
        return null;

    }

    public static List<GreNodeModel> queryAllCWVnode() {
        List<GreNodeModel> list = LitePal.where("node_name like ?", "CWV")
                .find(GreNodeModel.class);

        return list;

    }

    public static GreNodeModel queryETHnode() {
        List<GreNodeModel> list = LitePal.where("isUsing like ? and node_name like ?"
                , "1", "ETH")
                .find(GreNodeModel.class);
        if (CheckedUtils.INSTANCE.nonEmpty(list)) {
            return list.get(0);
        }


        List<GreNodeModel> defList = LitePal.where("is_def like ? and node_name like ?"
                , "1", "ETH")
                .find(GreNodeModel.class);
        if (CheckedUtils.INSTANCE.nonEmpty(defList)) {
            return defList.get(0);
        }
        return null;
    }

    public static List<GreNodeModel> queryAllETHnode() {
        List<GreNodeModel> list = LitePal.where("node_name like ?", "ETH")
                .find(GreNodeModel.class);
        return list;
    }

    public static void delete(GreNodeModel nodeModel) {
        nodeModel.delete();
    }

    public static void update(GreNodeModel greNodeModel) {

        ContentValues using = new ContentValues();
        using.put("node_url", greNodeModel.getNode_url());
        LitePal.update(GreNodeModel.class, using, greNodeModel.getId());
    }


    public static void restoreGnodeModels(List<GreNodeModel> nodeModels) {
        if (CheckedUtils.INSTANCE.isEmpty(nodeModels)) {
            return;
        }

        for (GreNodeModel nodeModel : nodeModels) {
            ContentValues using = new ContentValues();
            using.put("isUsing", nodeModel.isUsing());
            LitePal.update(GreNodeModel.class, using, nodeModel.getId());
        }


    }
}