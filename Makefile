JFX_PATH = /home/susithemafk/Desktop/ija-project/JavaFX/javafx-sdk-21.0.7/lib
JFX_MODULES = javafx.controls,javafx.fxml,javafx.graphics

MAIN_CLASS = ija.Main

compile:
	mkdir -p bin
	javac -encoding UTF-8 -d bin --module-path "$(JFX_PATH)" --add-modules $(JFX_MODULES) -source 21 -target 21 $(shell find src -name '*.java')

run: compile
	@mkdir -p bin/ija/view/images/pipes
	@cp src/ija/view/images/*.png bin/ija/view/images/ 2>/dev/null || true
	@cp -r src/ija/view/images/pipes bin/ija/view/images/ 2>/dev/null || true
	
	java --module-path "$(JFX_PATH)" --add-modules $(JFX_MODULES) -cp bin $(MAIN_CLASS)

clean:
	rm -rf bin

.PHONY: compile run clean