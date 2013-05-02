package mdettlaff.javagit.core;

import org.joda.time.DateTime;

public class Creator {

	private final String name;
	private final String email;
	private final DateTime date;
	private final String timezone;

	public Creator(String name, String email, DateTime date, String timezone) {
		this.name = name;
		this.email = email;
		this.date = date;
		this.timezone = timezone;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public DateTime getDate() {
		return date;
	}

	public String getTimezone() {
		return timezone;
	}

	public byte[] toByteArray() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(' ');
		builder.append("<" + email + ">");
		builder.append(' ');
		builder.append(date.getMillis() / 1000);
		builder.append(' ');
		builder.append(timezone);
		return builder.toString().getBytes();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(' ');
		builder.append("<" + email + ">");
		return builder.toString();
	}
}
