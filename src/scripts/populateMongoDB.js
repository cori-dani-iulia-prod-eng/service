require('dotenv').config();
const { MongoClient } = require('mongodb');

async function main() {
    const uri = process.env.MONGODB_CONECTION_URL || "mongodb://root:example@localhost:27017/";
    console.log(`Connecting to MongoDB at ${uri}...`);

    const client = new MongoClient(uri);

    try {
        await client.connect();
        const database = client.db('furniture_inventory');
        const categoriesCollection = database.collection('categories');
        const furnitureCollection = database.collection('furniture');
        const suppliersCollection = database.collection('suppliers');

        function getRandomElement(arr) {
            if (!arr || arr.length === 0) {
                throw new Error("Array is empty or undefined");
            }
            return arr[Math.floor(Math.random() * arr.length)];
        }

        // Define categories with sequential IDs
        const categories = [
            { categoryCode: 1, name: "Chair" },
            { categoryCode: 2, name: "Table" },
            { categoryCode: 3, name: "Sofa" },
            { categoryCode: 4, name: "Bed" },
            { categoryCode: 5, name: "Cabinet" }
        ];

        // Insert categories into the `categories` collection
        await categoriesCollection.insertMany(categories);

        const suppliers = [
            { name: "Supplier 1", email: "supplier1@gmail.com", phone: "123-456-7890", address: "123 Main St, Bucharest, Romania"},
            { name: "Supplier 2", email: "supplier2@yahoo.com", phone: "234-567-8901", address: "124 Second St, Madrid, Spain"},
            { name: "Supplier 3", email: "supplier3@hotmail.com", phone: "345-678-9012", address: "125 Church St, Warsaw, Poland"},
            { name: "Supplier 4", email: "supplier4@outlook.com", phone: "456-789-0123", address: "126 Central St, Rome, Italy"},
            { name: "Supplier 5", email: "supplier5@icloud.com", phone: "567-890-1234", address: "127 School St, London, UK"},
            { name: "Supplier 6", email: "supplier6@gmail.com", phone: "678-901-2345", address: "128 Industry St, Cluj-Napoca, Romania"},
            { name: "Supplier 7", email: "supplier7@yahoo.com", phone: "789-012-3456", address: "129 Main St, Barcelona, Spain"},
            { name: "Supplier 8", email: "supplier8@hotmail.com", phone: "890-123-4567", address: "130 Second St, Krakow, Poland"},
            { name: "Supplier 9", email: "supplier9@outlook.com", phone: "901-234-5678", address: "131 Church St, Milan, Italy"},
            { name: "Supplier 10", email: "supplier10@icloud.com", phone: "012-345-6789", address: "132 Central St, Manchester, UK"},
            { name: "Supplier 11", email: "supplier11@gmail.com", phone: "123-456-7800", address: "133 School St, Timisoara, Romania"},
            { name: "Supplier 12", email: "supplier12@yahoo.com", phone: "234-567--8900", address: "134 Industry St, Valencia, Spain"},
            { name: "Supplier 13", email: "supplier13@hotmail.com", phone: "345-678-9001", address: "135 Main St, Gdansk, Poland"},
            { name: "Supplier 14", email: "supplier14@outlook.com", phone: "456-789-0012", address: "136 Second St, Naples, Italy"},
            { name: "Supplier 15", email: "supplier15@icloud.com", phone: "567-890-1230", address: "137 Church St, Birgham, UK"},
            { name: "Supplier 16", email: "supplier16@gmail.com", phone: "678-901-2344", address: "138 Central St, Iasi, Romania"},
            { name: "Supplier 17", email: "supplier17@yahoo.com", phone: "789-012-3455", address: "139 School St, Seville, Spain"},
            { name: "Supplier 18", email: "supplier18@hotmail.com", phone: "890-123-4566", address: "140 Industry St, Wroclaw, Poland"},
            { name: "Supplier 19", email: "supplier19@outlook.com", phone: "901-234-5677", address: "141 Main St, Turin, Italy"},
            { name: "Supplier 20", email: "supplier20@icloud.com", phone: "012-345-6788", address: "142 Second St, Leeds, UK"}
        ];

        const insertedSuppliers = await suppliersCollection.insertMany(suppliers);
        console.log('Inserted suppliers:', insertedSuppliers.insertedIds);

        const suppliersIds = Object.values(insertedSuppliers.insertedIds);
        const insertedSupplierDocs = await suppliersCollection.find({ _id: { $in: suppliersIds } }).toArray();
        console.log('Inserted suppliers docs:', insertedSupplierDocs);

        const materials = ["Wood", "Metal", "Glass", "Leather", "Plastic"];
        const descriptions = ["High quality", "Modern design", "Affordable", "Luxury feel", "Eco-friendly"];

        // Generate 1000 random furniture items
        let furnitureItems = [];
        let itemCount = {}; // Track counts per category for naming and SKU

        for (let i = 1; i <= 1000; i++) {
            let category = getRandomElement(categories);
            let supplier = getRandomElement(insertedSupplierDocs);

            // Track count per category
            if (!itemCount[category.categoryCode]) {
                itemCount[category.categoryCode] = 1;
            }

            let itemNumber = itemCount[category.categoryCode]++;
            let formattedNumber = itemNumber.toString().padStart(3, "0");

            let item = {
                name: `${category.name} ${formattedNumber}`, // e.g., "Chair 001"
                sku: `${category.name.toUpperCase()}-${formattedNumber}`, // e.g., "CHAIR-001"
                categoryCode: category.categoryCode,
                price: Math.floor(Math.random() * 500) + 50, // Random price between 50 - 550
                stockQuantity: Math.floor(Math.random() * 100) + 1, // Stock between 1 - 100
                material: getRandomElement(materials),
                description: getRandomElement(descriptions),
                supplierId: supplier._id
            };
            furnitureItems.push(item);
        }

        await furnitureCollection.insertMany(furnitureItems);

        console.log("Successfully inserted categories and 1000 categorized furniture items!");
    } finally {
        await client.close();
    }
}

main().catch(console.error);