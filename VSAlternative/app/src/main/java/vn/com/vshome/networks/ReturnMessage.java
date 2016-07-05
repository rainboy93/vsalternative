package vn.com.vshome.networks;

import vn.com.vshome.utils.Utils;

public class ReturnMessage {

	public int cmd;
	public int status;

	public int[] data = new int[250];

	public String str1;
	public String str2;
	public String str3;

	public ReturnMessage(byte[] buff) {
		int n = 0;

		this.cmd = Utils.Byte2Unsigned(buff[0]);
		this.status = Utils.Byte2Unsigned(buff[1]);

		byte[] b = new byte[250];
		for (int i = 0; i < 250; i++) {
			b[i] = buff[i + 2];
		}
		for (int i = 0; i < b.length; i++)
			this.data[i] = Utils.Byte2Unsigned(b[i]);

		byte[] b1 = new byte[50];
		for (int i = 0; i < 50; i++) {
			b1[i] = buff[i + 252];
		}
		this.str1 = new String(b1);
		for (n = 0; n < this.str1.length(); n++)
			if (this.str1.charAt(n) == '\0')
				break;
		this.str1 = this.str1.substring(0, n);

		byte[] b2 = new byte[50];
		for (int i = 0; i < 50; i++) {
			b2[i] = buff[i + 302];
		}
		this.str2 = new String(b2);
		for (n = 0; n < this.str2.length(); n++)
			if (this.str2.charAt(n) == '\0')
				break;
		this.str2 = this.str2.substring(0, n);

		byte[] b3 = new byte[50];
		for (int i = 0; i < 50; i++) {
			b3[i] = buff[i + 352];
		}
		this.str3 = new String(b3);
		for (n = 0; n < this.str3.length(); n++)
			if (this.str3.charAt(n) == '\0')
				break;
		this.str3 = this.str3.substring(0, n);

	}

}
