help:
	@echo "There is a precompiled version of the project in StuyGFX.jar."
	@echo "If you really wish to recompile, run 'make recompile'"
	@echo "Run 'make run' to run"
	@echo "Dependencies:"
	@echo "    ant >= 1.7"
	@echo "    javac >= 1.7"
	@echo "    java >= 1.7"

recompile: build.xml jar-in-jar-loader.zip
	@ant

run: StuyGFX.jar
	@java -jar StuyGFX.jar 3d.pgi

