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

        function getRandomElement(arr) {
            return arr[Math.floor(Math.random() * arr.length)];
        }

        // Define categories with sequential IDs
        const categories = [
            { id: 1, name: "Chair" },
            { id: 2, name: "Table" },
            { id: 3, name: "Sofa" },
            { id: 4, name: "Bed" },
            { id: 5, name: "Cabinet" }
        ];

        // Insert categories into the `categories` collection
        await categoriesCollection.insertMany(categories);

        const materials = ["Wood", "Metal", "Glass", "Leather", "Plastic"];
        const descriptions = ["High quality", "Modern design", "Affordable", "Luxury feel", "Eco-friendly"];

        // Generate 1000 random furniture items
        let furnitureItems = [];
        let itemCount = {}; // Track counts per category for naming and SKU

        for (let i = 1; i <= 1000; i++) {
            let category = getRandomElement(categories);

            // Track count per category
            if (!itemCount[category.id]) {
                itemCount[category.id] = 1;
            }

            let itemNumber = itemCount[category.id]++;
            let formattedNumber = itemNumber.toString().padStart(3, "0");

            let item = {
                name: `${category.name} ${formattedNumber}`, // e.g., "Chair 001"
                sku: `${category.name.toUpperCase()}-${formattedNumber}`, // e.g., "CHAIR-001"
                categoryId: category.id,
                price: Math.floor(Math.random() * 500) + 50, // Random price between 50 - 550
                stockQuantity: Math.floor(Math.random() * 100) + 1, // Stock between 1 - 100
                material: getRandomElement(materials),
                description: getRandomElement(descriptions)
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