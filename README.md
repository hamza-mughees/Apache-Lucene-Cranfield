# Apache Lucene Cranfield
## Prerequisites
Use the following commands to install `Java`.
```
apt-get update
apt-get install default-jdk
```
Now, use the below command to install `Maven`.
```
apt-get install maven
```
In order to execute peformance evaluation, you must also install `gcc` and `make`. Use the following commands.
```
apt install make
apt install build-essential
```
## Clone the Repo
Use the following command to clone this repository.
```
git clone https://github.com/hamza-mughees/Apache-Lucene-Cranfield.git
```
## Index and Search
In order to run the indexing of the 1400 Cranfield documents, and the searching with the 225 queries, run the following commands.
```
mvn package
java -jar target/LuceneCranfield-1.0.jar
```
## Evaluate Performance
Use the following commands to calculate the MAP for each output.
```
cd trec_eval
./trec_eval -m map ../cran/cranqrel_corrected ../results/[analyzer]/[scoring algorithm].txt
```
Similarly, to obtain the recall, use the following command.
```
./trec_eval -m recall ../cran/cranqrel_corrected ../results/[analyzer]/[scoring algorithm].txt
```
