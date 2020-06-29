TOOLS_FOLDER="$JAVA_HOME/tools"

for tool in $(cat "./tool_list")
do
	if [ -d "$TOOLS_FOLDER/$tool" ]
	then
		rm -fr "$TOOLS_FOLDER/$tool"
	fi
done

