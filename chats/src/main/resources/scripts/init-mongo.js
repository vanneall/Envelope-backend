db.createUser({
    user: "admin",
    pwd: "password",
    roles: [
        { role: "root", db: "admin" }
    ]
});

db = db.getSiblingDB('messenger');