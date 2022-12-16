echo "Update the submodule (Error_messages.json)."
git submodule update --init --recursive --remote

echo "Update the EM.cs via the resources generator."
start ./src/resources-generator/ResourceGenerator.exe java