// command
db.col.bulkWrite([{insertOne: {document: {a: 1}}},{insertOne: {document: {a: 1}}}]);
// clear
db.col.drop();