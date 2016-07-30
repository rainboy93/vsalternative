package vn.com.vshome.foscamsdk;

/**
 * ���磺HD818P���ͻ�ȡ��ProductAllInfoΪ���£�<br>
 * <result>0</result> <model>23</model> <modelName>HD818P</modelName>
 * <language>0</language> <sensorType>6</sensorType> <wifiType>1</wifiType>
 * <reserve1>0</reserve1> <reserve2>0</reserve2> <reserve3>0</reserve3>
 * <reserve4>0</reserve4> <sdFlag>1</sdFlag> <outdoorFlag>0</outdoorFlag>
 * <ptFlag>1</ptFlag> <zoomFlag>1</zoomFlag> <rs485Flag>0</rs485Flag>
 * <ioAlarmFlag>0</ioAlarmFlag> <onvifFlag>1</onvifFlag> <p2pFlag>1</p2pFlag>
 * <wpsFlag>1</wpsFlag> <audioFlag>1</audioFlag> <talkFlag>1</talkFlag>
 * <reserveFlag1>0</reserveFlag1> <reserveFlag2>0</reserveFlag2>
 * <reserveFlag3>0</reserveFlag3> <reserveFlag4>0</reserveFlag4>
 * <appVer>2.11.1.6</appVer>
 */
/**
 * ����ͷ�İ汾��һ����Ϣ��ProductAllInfo��
 * 
 * @author FuS
 * @date 2014-6-28 ����10:46:42
 */
public class CameraProductAllInfo {
	private String _model = "0";
	private String _modelName = "";
	private String _language = "0";
	private String _sensorType = "0";
	private String _wifiType = "1";
	/** �ƴ洢�������ͣ�0����֧�֣�1֧���ƴ洢��2֧�������ͣ�3��֧�� */
	private String _reserve1 = "0";
	private String _reserve2 = "0";
	private String _reserve3 = "0";
	private String _reserve4 = "0";
	private String _sdFlag = "1";
	private String _outdoorFlag = "1";
	private String _ptFlag = "1";
	private String _zoomFlag = "1";
	private String _rs485Flag = "1";
	private String _ioAlarmFlag = "1";
	private String _onvifFlag = "10";
	private String _p2pFlag = "1";
	private String _wpsFlag = "1";
	private String _audioFlag = "1";
	private String _talkFlag = "1";
	private String _reserveFlag1 = "0";
	private String _reserveFlag2 = "0";
	private String _reserveFlag3 = "0";
	private String _reserveFlag4 = "0";
	private String _appVer = "";

	public CameraProductAllInfo() {
	}

	// set
	public void setModel(String model) {
		this._model = model;
	}

	public void setModelName(String modelName) {
		this._modelName = modelName;
	}

	public void setLanguage(String language) {
		this._language = language;
	}

	public void setSensorType(String sensorType) {
		this._sensorType = sensorType;
	}

	public void setWifiType(String wifiType) {
		this._wifiType = wifiType;
	}

	public void setReserve1(String reserve1) {
		this._reserve1 = reserve1;
	}

	public void setReserve2(String reserve2) {
		this._reserve2 = reserve2;
	}

	public void setReserve3(String reserve3) {
		this._reserve3 = reserve3;
	}

	public void setReserve4(String reserve4) {
		this._reserve4 = reserve4;
	}

	public void setSDFlag(String sdFlag) {
		this._sdFlag = sdFlag;
	}

	public void setOutdoorFlag(String outdoorFlag) {
		this._outdoorFlag = outdoorFlag;
	}

	public void setPtFlag(String ptFlag) {
		this._ptFlag = ptFlag;
	}

	public void setZoomFlag(String zoomFlag) {
		this._zoomFlag = zoomFlag;
	}

	public void setRs485Flag(String rs485Flag) {
		this._rs485Flag = rs485Flag;
	}

	public void setIOAlarmFlag(String ioAlarmFlag) {
		this._ioAlarmFlag = ioAlarmFlag;
	}

	public void setOnvifFlag(String onvifFlag) {
		this._onvifFlag = onvifFlag;
	}

	public void setP2pFlag(String p2pFlag) {
		this._p2pFlag = p2pFlag;
	}

	public void setWpsFlag(String wpsFlag) {
		this._wpsFlag = wpsFlag;
	}

	public void setAudioFlag(String audioFlag) {
		this._audioFlag = audioFlag;
	}

	public void setTalkFlag(String talkFlag) {
		this._talkFlag = talkFlag;
	}

	public void setReserveFlag1(String reserveFlag1) {
		this._reserveFlag1 = reserveFlag1;
	}

	public void setReserveFlag2(String reserveFlag2) {
		this._reserveFlag2 = reserveFlag2;
	}

	public void setReserveFlag3(String reserveFlag3) {
		this._reserveFlag3 = reserveFlag3;
	}

	public void setReserveFlag4(String reserveFlag4) {
		this._reserveFlag4 = reserveFlag4;
	}

	public void setAppVer(String appVer) {
		this._appVer = appVer;
	}

	// get
	/** Model number */
	public String getModel() {
		return this._model;
	}

	/** Model Name */
	public String getModelName() {
		return this._modelName;
	}

	/** Main language */
	public String getLanguage() {
		return this._language;
	}

	/** Sensor type number */
	public String getSensorType() {
		return this._sensorType;
	}

	/** Wifi Type Number */
	public String getWifiType() {
		return this._wifiType;
	}

	/** Reserve */
	public String getReserve1() {
		return this._reserve1;
	}

	/** Reserve */
	public String getReserve2() {
		return this._reserve2;
	}

	/** Reserve */
	public String getReserve3() {
		return this._reserve3;
	}

	/** Reserve */
	public String getReserve4() {
		return this._reserve4;
	}

	/** Whether machine support sd card */
	public String getSDFlag() {
		return this._sdFlag;
	}

	/** Whether machine is outdoor */
	public String getOutdoorFlag() {
		return this._outdoorFlag;
	}

	/** Whether machine support pt����̨��Ԥ�õ㣬Ѳ��·���� */
	public String getPtFlag() {
		return this._ptFlag;
	}

	/** Whether machine support zoom */
	public String getZoomFlag() {
		return this._zoomFlag;
	}

	/** Whether machine support rs485�������̨�� */
	public String getRs485Flag() {
		return this._rs485Flag;
	}

	/** Whether machine support io alarm */
	public String getIOAlarmFlag() {
		return this._ioAlarmFlag;
	}

	/** Whether machine support onvif */
	public String getOnvifFlag() {
		return this._onvifFlag;
	}

	/** Whether machine support p2p */
	public String getP2pFlag() {
		return this._p2pFlag;
	}

	/** Whether machine support wps */
	public String getWpsFlag() {
		return this._wpsFlag;
	}

	/** Whether machine support audio-speak */
	public String getAudioFlag() {
		return this._audioFlag;
	}

	/** Whether machine support audio-talk */
	public String getTalkFlag() {
		return this._talkFlag;
	}

	/** reserve */
	public String getReserveFlag1() {
		return this._reserveFlag1;
	}

	/** reserve */
	public String getReserveFlag2() {
		return this._reserveFlag2;
	}

	/** reserve */
	public String getReserveFlag3() {
		return this._reserveFlag3;
	}

	/** reserve */
	public String getReserveFlag4() {
		return this._reserveFlag4;
	}

	/** Camera application version */
	public String getAppVer() {
		return this._appVer;
	}
}
