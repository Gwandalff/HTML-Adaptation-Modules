TOOLS_FOLDER="$JAVA_HOME/tools"

for tool in $(cat "./tool_list")
do
	if [ ! -d "$TOOLS_FOLDER/$tool" ]
	then
		mkdir "$TOOLS_FOLDER/$tool"
	fi
	cp ../$tool/target/*.jar "$TOOLS_FOLDER/$tool/"
done
