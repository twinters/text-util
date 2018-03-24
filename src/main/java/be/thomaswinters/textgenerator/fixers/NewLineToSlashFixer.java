package be.thomaswinters.textgenerator.fixers;

public class NewLineToSlashFixer implements ISentenceFixer {


	@Override
	public String fix(String text) {
		return text.replace("\n", " / ");
	}
	
}
