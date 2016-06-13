help:
	@echo "There is a precompiled version of the project in StuyGFX.jar."
	@echo "If you really wish to recompile, run 'make recompile'"
	@echo "Run 'make run' to run"
	@echo "Dependencies:"
	@echo "    ant >= 1.7"
	@echo "    javac >= 1.7"
	@echo "    java >= 1.7"
	@echo "Other 'make' options:"
	@echo "    runBasic"
	@echo "    monkey"
	@echo "    human"

recompile: build.xml jar-in-jar-loader.zip
	@ant

interpreter: StuyGFX.jar
	@java -jar StuyGFX.jar

runBasic: StuyGFX.jar
	@java -jar StuyGFX.jar script.mdl

monkey: StuyGFX.jar
	java -jar StuyGFX.jar monkeylights.mdl $(CURDIR)/monkey.obj

human: StuyGFX.jar
	java -jar StuyGFX.jar humanlights.mdl $(CURDIR)/human.obj 40

run: StuyGFX.jar
	java -jar StuyGFX.jar dronelights.mdl $(CURDIR)/Drone.obj 50
