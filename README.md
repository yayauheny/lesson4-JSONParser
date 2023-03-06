# JsonParser library
### Description
The JsonParser library can be used to convert objects to JSON and JSON to objects. 
To use the library, create an instance of the `JSONParser` class and call its two public methods: `fromJSON` and `toJSON`.
Note: the library can parse only objects with fields and ordinal arrays
## Usage
Here is an example of how to use the JSONParser class:

`JSONParser parser = new JSONParser();
parser.toJSON(filePath, ...objects);`

