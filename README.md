# Maverl API Automation Challenge

This framework consists of:

#### 2. API Automation of 3 endpoints of the Marvel API https://developer.marvel.com and their negative test cases

1. Fetch all Marvel characters
2. Fetch all Spider-Man comics
3. Fetch all X-Man characters

## Tools and technologies

* Java
* REST assured
* Maven

## How to run the tests locally

### Pre-requisites

* IntelliJ needs to be installed
* Java SDK 16 and JRE 8u361

### installation

1. Clone the repo
   ```sh
   git clone https://github.com/CarlaGomez/marvel-api-rest-assured.git
   ```
2. Rebuild the project and make sure the POM.xml dependencies are downloaded from Maven

3. Go to the `BaseClass`, and paste your own generated timestamp, public key and md5 hash:
   ```
    timestamp="<Generate a timestamp and paste it here>", //Generate a timestamp on this page https://timestampgenerator.com/
    publicKey="<Paste your Marvel API public key here>",
    hash="<Generate a md5 hash and paste it here>", //Generate a md5 hash on this page https://www.md5hashgenerator.com/
    //To generate a hash, paste your previously generated timestamp, your private key and your public key all together, then copy the md5 hash
   ```
4. Run the tests

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch
3. Commit your Changes
4. Push to the Branch 
5. Open a Pull Request

