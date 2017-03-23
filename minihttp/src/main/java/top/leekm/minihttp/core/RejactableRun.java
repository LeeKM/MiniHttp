package top.leekm.minihttp.core;

/**
 * Created by lkm on 2017/3/20.
 */

public abstract class RejactableRun extends Run
        implements MiniHttpExecutor.Rejactable {

    @Override
    public void onReject() {
        Log.log("reject", this.toString());
    }

}
