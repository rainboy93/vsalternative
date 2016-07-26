package vn.com.vshome.flexibleadapter;

import java.io.Serializable;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.DeviceSelectCallback;
import vn.com.vshome.database.DeviceState;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.view.ProgressHUD;

/**
 * This class will benefit of the already implemented methods (getter and setters) in
 * {@link AbstractFlexibleItem}.
 * <p/>
 * It is used as Base item for all example models.
 */
public abstract class AbstractControlItem<VH extends FlexibleViewHolder>
        extends AbstractFlexibleItem<VH>
        implements Serializable {

    private static final long serialVersionUID = -6882745111884490060L;

    private String id;
    private String title;
    private String subtitle;
    public long deviceId;
    public LightingDevice device;
    public DeviceState deviceState;
    public boolean isControl = true;
    public ControlLayoutManager layoutManager;
    public boolean isSelected = false;
    public DeviceState tempState;

    public AbstractControlItem(String id) {
        this.id = id;
        tempState = new DeviceState();
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof AbstractControlItem) {
            AbstractControlItem inItem = (AbstractControlItem) inObject;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", title=" + title;
    }

    public void startSendControlMessage(LightingDevice device) {
        ProgressHUD.showLoading(VSHome.activity);
        TimeOutManager.getInstance().startCountDown(new TimeOutManager.TimeOutCallback() {
            @Override
            public void onTimeOut() {
                ProgressHUD.hideLoading(VSHome.activity);
                Toaster.showMessage(VSHome.activity, "asdfasdf");
            }
        }, 5);
        CommandMessage message = new CommandMessage();
        message.setControlMessage(device);
        VSHome.socketManager.receiveThread.currentControlDeviceId = device.getId().intValue();
        VSHome.socketManager.sendMessage(message);
    }

    public DeviceSelectCallback callback;
}