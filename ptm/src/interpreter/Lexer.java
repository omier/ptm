package interpreter;

public class Lexer {
	Server server;

	public Lexer(Server server) {
		this.server = server;
	}

	public PeekableScanner execute(String text) {
		text = text.replaceAll("\\{", "\\ { ").replaceAll("\\}", "\\ } ").replaceAll("\\>", "\\ > ")
				.replaceAll("\\<", "\\ < ").replaceAll("\\+", "\\ + ").replaceAll("\\-", "\\ - ")
				.replaceAll("\\*", "\\ * ").replaceAll("\\/", "\\ / ").replaceAll("\\(", "\\ ( ")
				.replaceAll("\\)", "\\ ) ").replaceAll("\\=", "\\ = ").replaceAll("==", "+is+")
				.replaceAll("!=", "+not+").replaceAll("<=", "+lessEqual+").replaceAll(">=", "+bigEqual+")
				.replaceAll("<[^=]", "+big+").replaceAll(">[^=]", "+small+").trim();

		PeekableScanner lexer = new PeekableScanner(text);
		lexer.useDelimiter(
				"((?<=[a-zA-Z0-9={}\"])\\s+(?=[a-zA-Z0-9={}\"(])|[\\n\\r]+|(?<=[{}])|(?=[{}])|(?<=[=])|(?=[=]))(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

		return lexer;
	}

}