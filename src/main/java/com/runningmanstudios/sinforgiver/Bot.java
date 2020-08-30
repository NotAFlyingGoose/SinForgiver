package com.runningmanstudios.sinforgiver;

import com.runningmanstudios.sinforgiver.commandUtil.Command;
import com.runningmanstudios.sinforgiver.data.Data;
import com.runningmanstudios.sinforgiver.data.DataBase;
import com.runningmanstudios.sinforgiver.data.SuperData;
import com.runningmanstudios.sinforgiver.events.EventDetector;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Bot {
    public static final int COMMON = 1;
    public static final int UNCOMMON = 2;
    public static final int RARE = 3;
    public static final int SUPERRARE = 4;
    public static final int HACKER = 5;
    String dataLocation;
    EventDetector eventDetector;
    public DataBase users;
    public DataBase items;
    public DataBase data;
    public JDA jda;

    public Bot(String dataLocation) {
        this.dataLocation = dataLocation;
        File location = new File(System.getProperty("user.home") + dataLocation);
        location.mkdirs();

        String token;
        JSONObject info;
        try {
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();

            FileReader reader = new FileReader(location.getAbsolutePath()+"\\info.json");
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            info = (JSONObject) obj;
        } catch (Exception e) {
            throw new IllegalArgumentException("There was an error while trying to locate the json "+location.getAbsolutePath()+"\\info.json");
        }
        try {
            String raw = (String) info.get("token");
            if (raw == null) {
                throw new Exception();
            }
            token = raw;
        } catch (Exception e) {
            throw new IllegalArgumentException("There was an error while trying to get the string \"token\" from the json "+location.getAbsolutePath()+"\\info.json");
        }

        users = new DataBase(dataLocation+"\\users.json");
        items = new DataBase(dataLocation+"\\items.json");
        items.clearContent();
        addItemData(items);
        data = new DataBase(dataLocation+"\\data.json");
        data.clearContent();
        addData(data);
        data.writeContent();
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token).build();

            eventDetector = new EventDetector(this);
            jda.addEventListener(eventDetector);

        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public void addCommand(Command command) {
        eventDetector.addCommand(command);
    }

    public void addData(DataBase data) {
        data.addSection("shop",
                new Data("item_dildo", 20),
                new Data("item_tube", 20),
                new Data("item_game", 20),
                new Data("item_expensive_whisky", 50),
                new Data("item_fishing_rod", 15),
                new Data("item_pill", 25),
                new Data("item_band_aid", 15),
                new Data("item_shrek", 50),
                new Data("item_trophy", 20));
        data.addSection("adopt",
                new Data("pet_crab", 20),
                new Data("pet_panda", 20),
                new Data("pet_duck", 20),
                new Data("pet_squid", 20),
                new Data("pet_french_frog", 15),
                new Data("pet_gary", 25));
        data.addSection("monsters",
                new SuperData("Axe Man")
                        .addData(new Data("logo", "🪓"))
                        .addData(new Data("base", 5))
                        .addData(new Data("says", "Heeere's Johnny!\n"))
                        .addData(new Data("onMagic", "${name}? Darling? Light, of my life. I'm not gonna hurt ya. I'm just going to bash your brains in."))
                        .addData(new Data("onRank", "No need to rub it in, Mr. ${name}.")),
                new SuperData("Cultist")
                        .addData(new Data("logo", "🕵️‍♂️"))
                        .addData(new Data("base", 2))
                        .addData(new Data("says", "Join our religion for just $4.99 a month!"))
                        .addData(new Data("onMagic", "god (that's me) awaits you"))
                        .addData(new Data("onRank", "our friends would be happy to meat someone like you")),
                new SuperData("Soviet Duck Supreme")
                        .addData(new Data("logo", "🦆"))
                        .addData(new Data("base", 5))
                        .addData(new Data("says", "Unite the people *quack* *quack*")),
                new SuperData("Extreme Horror of an Unimaginable Bulk Capacity Ratio")
                        .addData(new Data("logo", "😱"))
                        .addData(new Data("base", 17))
                        .addData(new Data("says", "reeeeeeeeeeeeeeeeee"))
                        .addData(new Data("onMagic", "reeeeeeeeee"))
                        .addData(new Data("onRank", "ok this is getting anoying now")),
                new SuperData("Milk Man")
                        .addData(new Data("logo", "🥛"))
                        .addData(new Data("base", 6))
                        .addData(new Data("says", "私は牛乳です")),
                new SuperData("Orange Juice Guy")
                        .addData(new Data("logo", "🥛"))
                        .addData(new Data("base", 2))
                        .addData(new Data("says", "オレンジ！")),
                new SuperData("Life Insurance Agent")
                        .addData(new Data("logo", "📝"))
                        .addData(new Data("base", 18))
                        .addData(new Data("says", "Sign the contract"))
                        .addData(new Data("onMagic", "c'mon what u waiting for, sign your initials right here."))
                        .addData(new Data("onRank", "look man this lasts your full policy term, the accelerated death annuity Beneficiary benifit is not going to help itself")),
                new SuperData("Car Salesman")
                        .addData(new Data("logo", "🚙"))
                        .addData(new Data("base", 6))
                        .addData(new Data("says", "This bad boy can fuck you in the ass so hard."))
                        .addData(new Data("onRank", "just like my uncle \uD83D\uDE2D")),
                new SuperData("Literaly just a monkey")
                        .addData(new Data("logo", "🐵"))
                        .addData(new Data("base", 2))
                        .addData(new Data("says", "dude, im literaly just a monkey"))
                        .addData(new Data("onMagic", "he is just a monkey tho")),
                new SuperData("Scowling Vine")
                        .addData(new Data("logo", "🌿"))
                        .addData(new Data("base", 8))
                        .addData(new Data("says", "*Scowl*"))
                        .addData(new Data("onMagic", "honestly why did i add this to the game"))
                        .addData(new Data("onRank", "it really adds nothing of value")),
                new SuperData("Insane Mutt")
                        .addData(new Data("logo", "🐕"))
                        .addData(new Data("base", -500))
                        .addData(new Data("says", "*rugh* *rugh* *rugh* life is too rough for me kill me please"))
                        .addData(new Data("onMagic", "god please"))
                        .addData(new Data("onRank", "o my good just KIIL ME ALREADY")),
                new SuperData("a Ghost")
                        .addData(new Data("logo", "👻"))
                        .addData(new Data("base", 5))
                        .addData(new Data("says", "will you be my simp uwu?"))
                        .addData(new Data("onMagic", "pwease be my simp OwO"))
                        .addData(new Data("onRank", "uuwwuuuu BE MY SIMP NOOOOW!")),
                new SuperData("50 Foot tall Elephant")
                        .addData(new Data("logo", "🐘"))
                        .addData(new Data("base", 20))
                        .addData(new Data("says", "こんにちは私の赤ちゃん、こんにちは私の蜂蜜こんにちは私のラグタイム、夏のギャルワイヤーでキスを送って"))
                        .addData(new Data("onMagic", "象が好き"))
                        .addData(new Data("onRank", "兄は弱虫です")),
                new SuperData("20 Foot tall Elephant")
                        .addData(new Data("logo", "🐘"))
                        .addData(new Data("base", 15))
                        .addData(new Data("says", "*crying* i'm so short"))
                        .addData(new Data("onMagic", "Why must life be this way"))
                        .addData(new Data("onRank", "my brother always makes fun of me")),
                new SuperData("Bowelcat")
                        .addData(new Data("logo", "🐈"))
                        .addData(new Data("base", 7))
                        .addData(new Data("says", "Ughhh i need to focking SHITTTTT"))
                        .addData(new Data("onMagic", "WHer is ThE FoCkING TOILET!"))
                        .addData(new Data("onRank", "omg im ded xP")),
                new SuperData("Amphibian Mocking Bull")
                        .addData(new Data("logo", "🐮"))
                        .addData(new Data("base", 10))
                        .addData(new Data("says", "${said}"))
                        .addData(new Data("onMagic", "${said}"))
                        .addData(new Data("onRank", "${said}")),
                new SuperData("Primitive Half Boulder Man")
                        .addData(new Data("logo", "🌑"))
                        .addData(new Data("base", 18))
                        .addData(new Data("says", "*silence*"))
                        .addData(new Data("onMagic", "*silence*"))
                        .addData(new Data("onRank", "*silence*")),
                new SuperData("Pantheerian Shadow Claw")
                        .addData(new Data("logo", "🦞"))
                        .addData(new Data("base", 2))
                        .addData(new Data("says", "(ausie accent) hey bloke ya'ah a fuckin' cunt 'n i'm about ta root ya up Fahkin' too right, cobber.")),
                new SuperData("Robot Alligator")
                        .addData(new Data("logo", "🐊🤖"))
                        .addData(new Data("base", 18))
                        .addData(new Data("says", "beep"))
                        .addData(new Data("onMagic", "beep"))
                        .addData(new Data("onRank", "beep")),
                new SuperData("Crocodile Cyborg")
                        .addData(new Data("logo", "🐊🤖"))
                        .addData(new Data("base", 19))
                        .addData(new Data("says", "beep (but cooler)"))
                        .addData(new Data("onMagic", "beep"))
                        .addData(new Data("onRank", "beep")),
                new SuperData("Crocogator with Lazers")
                        .addData(new Data("logo", "🐊🔦"))
                        .addData(new Data("base", 20))
                        .addData(new Data("says", "prepare to focking DIEEEEEEEEEEE"))
                        .addData(new Data("onMagic", "human, you think to be better than i, the great Lazer Crocodile. no you FOOL!"))
                        .addData(new Data("onRank", "STUPID HUMAN. I SHUN YOUR FOCKING AWFUL MISTAKES!!!")),
                new SuperData("Lizard Weeb")
                        .addData(new Data("logo", "🦎🤓"))
                        .addData(new Data("base", 1))
                        .addData(new Data("says", "do you even mlg bro"))
                        .addData(new Data("onMagic", "gandalf is way better than you, fuck-face"))
                        .addData(new Data("onRank", "i have so many babes bro, its crazy. uhhhh they're not here rn tho")),
                new SuperData("Introverted Spider")
                        .addData(new Data("logo", "🕷️😰"))
                        .addData(new Data("base", 3))
                        .addData(new Data("says", "go away, i was doing netflix and chill")),
                new SuperData("Girl")
                        .addData(new Data("logo", "👧"))
                        .addData(new Data("base", 7))
                        .addData(new Data("says", "idk what girls say i never talked to one"))
                        .addData(new Data("onMagic", "imagine talking to a girl \uD83D\uDE44"))
                        .addData(new Data("onRank", "dude :D could u even imagine being nice to a female woman LMAFO... SIIIIIIIMMMP")),
                new SuperData("Snooping Dog")
                        .addData(new Data("logo", "🐕"))
                        .addData(new Data("base", 17))
                        .addData(new Data("says", "OwO hewwo owo my fewwow human, why do you faiw to pet me")),
                new SuperData("Corn-Cake Karate Man")
                        .addData(new Data("logo", "🥋🍰"))
                        .addData(new Data("base", 3))
                        .addData(new Data("says", "ディオ！"))
                        .addData(new Data("onMagic", "無駄！無駄！無駄！無駄！無駄！無駄！無駄！"))
                        .addData(new Data("onRank", "あなたはばかです、私はあなたの無能を笑います")),
                new SuperData("Duck-Like toilet seat")
                        .addData(new Data("logo", "🚽🦆"))
                        .addData(new Data("base", 9))
                        .addData(new Data("says", "*quaaaaaaaaaaaackkkk*")),
                new SuperData("Bat-Shit Crazy Amish Folk")
                        .addData(new Data("logo", "👨‍🌾"))
                        .addData(new Data("base", 13))
                        .addData(new Data("says", "we attack with prayers and pitchforks"))
                        .addData(new Data("onMagic", "have you ever herda' GOD WITCH"))
                        .addData(new Data("onRank", "darla' bring da kids out er' tel'em we'r eatn' MAN tonight")),
                new SuperData("Mentally Impaired Man with a Clown Fetish")
                        .addData(new Data("logo", "🤡"))
                        .addData(new Data("base", 7))
                        .addData(new Data("says", "Society")),
                new SuperData("Book of Corrupt Broken Promises")
                        .addData(new Data("logo", "📕"))
                        .addData(new Data("base", 5))
                        .addData(new Data("says", "liiiieeesssss"))
                        .addData(new Data("onMagic", "wow i thought you would be able to beat a book, i mean come on! that was horrible!"))
                        .addData(new Data("onRank", "you bitch")),
                new SuperData("Jeff")
                        .addData(new Data("logo", "👨"))
                        .addData(new Data("base", 1))
                        .addData(new Data("says", "Hey neighbor! wanna have a backyard bbq?"))
                        .addData(new Data("onMagic", "Wow what's that! that looks like magic, hey maybe you should become one of those \"magic people\" in the circus")));
        JSONArray dungeonLoot = new JSONArray();
        dungeonLoot.add("item_shit");
        dungeonLoot.add("item_swimsuit");
        dungeonLoot.add("item_dildo");
        dungeonLoot.add("item_tube");
        dungeonLoot.add("item_fishing_rod");
        dungeonLoot.add("item_trash");
        dungeonLoot.add("item_cash");
        dungeonLoot.add("pet_crab");
        dungeonLoot.add("pet_squid");
        dungeonLoot.add("item_contract");
        dungeonLoot.add("pet_french_frog");
        dungeonLoot.add("item_shrek");
        dungeonLoot.add("item_luck");
        dungeonLoot.add("item_expensive_whisky");
        data.addSection("treasure",
                new Data("dungeon", dungeonLoot));
    }

    public void addItemData(DataBase data) {
        data.addSection("item_dildo",
                new Data("name", "Purple Back Massager"),
                new Data("icon", "🍆"),
                new Data("rarity", COMMON));
        data.addSection("item_tube",
                new Data("name", "Cylinder With Hole on the Top"),
                new Data("icon", "🍑"),
                new Data("rarity", COMMON));
        data.addSection("item_swimsuit",
                new Data("name", "Oh Yeah"),
                new Data("icon", "🩱"),
                new Data("rarity", RARE));
        data.addSection("item_stalin",
                new Data("name", "Soviet Medal of Honor"),
                new Data("icon", "🎖️"),
                new Data("rarity", RARE));
        data.addSection("item_luck",
                new Data("name", "Luck"),
                new Data("icon", "🎱"),
                new Data("rarity", RARE));
        data.addSection("item_game",
                new Data("name", "Used Video Game"),
                new Data("icon", "🎮"),
                new Data("rarity", SUPERRARE));
        data.addSection("item_cash",
                new Data("name", "Bag of Foreign Cash"),
                new Data("icon", "💰"),
                new Data("rarity", RARE));
        data.addSection("item_shit",
                new Data("name", "Shit"),
                new Data("icon", "💩"),
                new Data("rarity", COMMON));
        //pets
        data.addSection("pet_french_frog",
                new Data("name", "Pet LèFrog"),
                new Data("icon", "🐸"),
                new Data("rarity", SUPERRARE));
        data.addSection("pet_alien",
                new Data("name", "Pet Alien"),
                new Data("icon", "👾"),
                new Data("rarity", RARE));
        data.addSection("pet_crab",
                new Data("name", "Pet Crab"),
                new Data("icon", "🦀"),
                new Data("rarity", UNCOMMON));
        data.addSection("pet_lobster",
                new Data("name", "Pet Lobster"),
                new Data("icon", "🦞"),
                new Data("rarity", HACKER));
        data.addSection("pet_duck",
                new Data("name", "Pet Quack"),
                new Data("icon", "🦆"),
                new Data("rarity", UNCOMMON));
        data.addSection("pet_squid",
                new Data("name", "Pet Squid"),
                new Data("icon", "🦑"),
                new Data("rarity", UNCOMMON));
        data.addSection("pet_pickle",
                new Data("name", "Pet Pickle"),
                new Data("icon", "🥒"),
                new Data("rarity", HACKER));
        data.addSection("pet_panda",
                new Data("name", "Pet Panda Bear"),
                new Data("icon", "🐼"),
                new Data("rarity", UNCOMMON));
        data.addSection("pet_gary",
                new Data("name", "Pet Snail"),
                new Data("icon", "🐌"),
                new Data("rarity", SUPERRARE));
        data.addSection("pet_horse",
                new Data("name", "Pet wtf is this?"),
                new Data("icon", "🦄"),
                new Data("rarity", SUPERRARE));
        data.addSection("item_expensive_whisky",
                new Data("name", "Bottle of MacCutcheon Whiskey"),
                new Data("icon", "🍾"),
                new Data("rarity", HACKER));
        data.addSection("item_trophy",
                new Data("name", "Participation Trophie"),
                new Data("icon", "🏆"),
                new Data("rarity", COMMON));
        data.addSection("item_fishing_rod",
                new Data("name", "Fishing Rod"),
                new Data("icon", "🎣"),
                new Data("rarity", UNCOMMON));
        data.addSection("item_contract",
                new Data("name", "Fishy Contract"),
                new Data("icon", "📝"),
                new Data("rarity", RARE));
        data.addSection("item_talent",
                new Data("name", "Talent"),
                new Data("icon", "🎵"),
                new Data("rarity", RARE));
        data.addSection("item_pill",
                new Data("name", "Magic Pill"),
                new Data("icon", "💊"),
                new Data("rarity", RARE));
        data.addSection("item_band_aid",
                new Data("name", "Band Aid"),
                new Data("icon", "🩹"),
                new Data("rarity", UNCOMMON));
        //piriting items
        data.addSection("item_city",
                new Data("name", "An Entire Fucking City"),
                new Data("icon", "🏙️"),
                new Data("rarity", HACKER));
        data.addSection("item_atom_bomb",
                new Data("name", "Hitler's Secret Atom Bomb"),
                new Data("icon", "💣"),
                new Data("rarity", HACKER));
        data.addSection("item_mounument",
                new Data("name", "A National Mounument"),
                new Data("icon", "🗽"),
                new Data("rarity", SUPERRARE));
        data.addSection("item_flash_maker",
                new Data("name", "Particle Accelerator"),
                new Data("icon", "🏟️"),
                new Data("rarity", SUPERRARE));
        data.addSection("item_moon_trip",
                new Data("name", "Round Trip to the Moon"),
                new Data("icon", "🎟️"),
                new Data("rarity", RARE));
        data.addSection("item_historical",
                new Data("name", "Obscure Object of Historical Significance"),
                new Data("icon", "🗿"),
                new Data("rarity", RARE));
        data.addSection("item_trash",
                new Data("name", "It's literaly just trash"),
                new Data("icon", "🧻"),
                new Data("rarity", COMMON));
        //pet stuff
        data.addSection("item_treat",
                new Data("name", "Pet Treat"),
                new Data("icon", "🍖"),
                new Data("rarity", UNCOMMON));
        data.addSection("item_fishy",
                new Data("name", "Fish"),
                new Data("icon", "🐟"),
                new Data("rarity", UNCOMMON));
        data.addSection("item_dead_birb",
                new Data("name", "Dead Birb"),
                new Data("icon", "🐦"),
                new Data("rarity", UNCOMMON));
        data.addSection("item_dead_squirb",
                new Data("name", "Dead Squirb"),
                new Data("icon", "🐿️"),
                new Data("rarity", UNCOMMON));
        data.addSection("item_shrek",
                new Data("name", "Layered Onion"),
                new Data("icon", "🧅"),
                new Data("rarity", RARE));
        data.writeContent();
    }

    public String getItemsByRarity(Iterable<Object> itemIds, float multiplier) {
        List<String> allitems = new LinkedList<>();
        List<String> commonitems = new LinkedList<>();
        List<String> uncommonitems = new LinkedList<>();
        List<String> rareitems = new LinkedList<>();
        List<String> extremelyrareitems = new LinkedList<>();
        List<String> hackeritems = new LinkedList<>();
        for (Object item : itemIds) {
            int r = getItemRarity(item.toString());
            if (r == 1){
                commonitems.add(item.toString());
            } else if (r == 2){
                uncommonitems.add(item.toString());
            } else if (r == 3){
                rareitems.add(item.toString());
            } else if (r == 4){
                extremelyrareitems.add(item.toString());
            } else if (r == 5){
                hackeritems.add(item.toString());
            }
        }
        allitems.addAll(commonitems);
        allitems.addAll(uncommonitems);
        allitems.addAll(rareitems);
        allitems.addAll(extremelyrareitems);
        allitems.addAll(hackeritems);
        double result = Math.random();
        result = Math.pow(result, multiplier);
        result *= allitems.size();
        result = Math.floor(result);
        return allitems.get((int) result);
    }

    public String ItemRarityString(String rarity) {
        return ItemRarityName(rarity)+" "+ItemRaritySymbol(rarity);
    }

    public String ItemRaritySymbol(String rarity) {
        return switch (rarity) {
            case "1" -> "\uD83D\uDFE2";
            case "2" -> "\uD83D\uDFE1";
            case "3" -> "\uD83D\uDD34";
            case "4" -> "⚫";
            case "5" -> "⛔";
            default -> "❓";
        };
    }

    public String ItemRarityName(String rarity) {
        return switch (rarity) {
            case "1" -> "common";
            case "2" -> "uncommon";
            case "3" -> "rare";
            case "4" -> "super rare";
            case "5" -> "hacker";
            default -> "unknown";
        };
    }

    public int getItemRarity(String itemId) {
        return ((Number)items.getSection(itemId).get("rarity")).intValue();
    }
}
