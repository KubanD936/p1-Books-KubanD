CREATE TABLE "books" (
    "BookId" INT PRIMARY KEY NOT NULL,
    "Name" VARCHAR
);

CREATE TABLE "library" (
    "libraryId" INT NOT NULL,
    "Title" VARCHAR NOT NULL,
    "BookId" INT NOT NULL,
    CONSTRAINT "PK_Library" PRIMARY KEY ("LibraryId"),
    CONSTRAINT "FK_LibraryBookId" FOREIGN KEY ("BookId") REFERENCES "book" ("BookId") ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE INDEX "IFK_LibraryBookId" ON "library" ("BookId");

INSERT INTO "book" VALUES (1, 'Game of Thrones');
INSERT INTO "book" VALUES (2, 'Harry Potter');