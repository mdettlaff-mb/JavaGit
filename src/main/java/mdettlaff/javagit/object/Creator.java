package mdettlaff.javagit.object;

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

public class Creator {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

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
		ByteArrayBuilder bytes = new ByteArrayBuilder();
		bytes.string(name);
		bytes.string(" ");
		bytes.string("<").string(email).string(">");
		bytes.string(" ");
		bytes.string(String.valueOf(date.getMillis() / 1000));
		bytes.string(" ");
		bytes.string(timezone);
		return bytes.build();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(' ');
		builder.append('<').append(email).append('>');
		builder.append(' ');
		builder.append(new SimpleDateFormat(DATE_FORMAT).format(date.toDate()));
		builder.append(' ');
		builder.append(timezone);
		return builder.toString();
	}
}
