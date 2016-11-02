package com.haoweixiu.dto;

public class UpdateApkInfoDto {

	private String message;
	private String success;
	private String version;
	private String url;
	private String caption;
	private String date;

	public UpdateApkInfoDto() {
		// TODO Auto-generated constructor stub
		super();
	}

	public UpdateApkInfoDto(String message, String success, String version,
							String url, String caption, String date) {
		super();
		this.message = message;
		this.success = success;
		this.version = version;
		this.url = url;
		this.caption = caption;
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "UpdateApkInfoDto [message=" + message + ", success=" + success
				+ ", version=" + version + ", url=" + url + ", caption="
				+ caption + ", date=" + date + "]";
	}

}
