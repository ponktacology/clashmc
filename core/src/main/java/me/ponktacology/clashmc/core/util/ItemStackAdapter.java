package me.ponktacology.clashmc.core.util;

import com.google.gson.*;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;



import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("deprecation")
public class ItemStackAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {

    public static final String ID = "id";
    public static final String COUNT = "count";
    public static final String DAMAGE = "damage";

    public static final String NAME = "name";
    public static final String LORE = "lore";

    public static final String ENCHANTS = "enchants";

    public static final String REPAIRCOST = "repaircost";

    public static final String BOOK_TITLE = "title";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_PAGES = "pages";

    public static final String LEATHER_ARMOR_COLOR = "color";

    public static final String MAP_SCALING = "scaling";

    public static final String SKULL_OWNER = "skull";

    public static final String POTION_EFFECTS_OLD = "effects";
    public static final String POTION_EFFECTS = "potion-effects";

    public static final String STORED_ENCHANTS = "stored-enchants";

    // -------------------------------------------- //
    // OTHER CONSTANTS
    // -------------------------------------------- //

    public static final int DEFAULT_ID;
    public static final int DEFAULT_COUNT;
    public static final int DEFAULT_DAMAGE;

    static {
        ItemStack stack = createItemStack();
        DEFAULT_ID = stack.getTypeId();
        DEFAULT_COUNT = stack.getAmount();
        DEFAULT_DAMAGE = stack.getDurability();
    }

    // -------------------------------------------- //
    // GSON INTERFACE IMPLEMENTATION
    // -------------------------------------------- //

    
    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return erialize(src);
    }

    
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return erialize(json);
    }

    // -------------------------------------------- //
    // WRITE
    // -------------------------------------------- //

    
    public static JsonObject erialize( ItemStack stack) {
        // Check for "nothing"
        if (stack == null)
            return null;
        if (stack.getTypeId() == 0)
            return null;
        if (stack.getAmount() == 0)
            return null;

        // Create a new JsonObject
        JsonObject json = new JsonObject();

        // Transfer data from stack to json
        transferAll(stack, json, true);

        return json;
    }

    
    public static ItemStack erialize( JsonElement jsonElement) {
        // Check for "nothing"
        if (jsonElement == null)
            return null;

        // Must be a JsonObject
        if (jsonElement.isJsonObject() == false)
            return null;
        JsonObject json = jsonElement.getAsJsonObject();

        // Create a new ItemStack
        ItemStack stack = createItemStack();

        // Transfer data from json to stack
        transferAll(stack, json, false);

        return stack;
    }

    // -------------------------------------------- //
    // NOARG STACK CONSTRUCTOR
    // -------------------------------------------- //


    public static ItemStack createItemStack() {
        return new ItemStack(0);
    }

    // -------------------------------------------- //
    // ALL
    // -------------------------------------------- //

    public static void transferAll( ItemStack stack,  JsonObject json, boolean stack2json) {
        transferBasic(stack, json, stack2json);

        ItemMeta meta = stack.getItemMeta();
        transferMeta(meta, json, stack2json);

        if (stack2json == false) {
            stack.setItemMeta(meta);
        }
    }

    // -------------------------------------------- //
    // BASIC
    // -------------------------------------------- //

    public static void transferBasic( ItemStack stack,  JsonObject json, boolean stack2json) {
        transferId(stack, json, stack2json);
        transferCount(stack, json, stack2json);
        transferDamage(stack, json, stack2json);
    }

    // -------------------------------------------- //
    // BASIC: ID
    // -------------------------------------------- //

    public static void transferId( ItemStack stack,  JsonObject json, boolean stack2json) {
        if (stack2json) {
            int id = stack.getTypeId();
            if (id == DEFAULT_ID)
                return;
            json.addProperty(ID, id);
        } else {
            JsonElement element = json.get(ID);
            if (element == null)
                return;
            stack.setTypeId(element.getAsInt());
        }
    }

    // -------------------------------------------- //
    // BASIC: COUNT
    // -------------------------------------------- //

    public static void transferCount( ItemStack stack,  JsonObject json, boolean stack2json) {
        if (stack2json) {
            int count = stack.getAmount();
            if (count == DEFAULT_COUNT)
                return;
            json.addProperty(COUNT, count);
        } else {
            JsonElement element = json.get(COUNT);
            if (element == null)
                return;
            stack.setAmount(element.getAsInt());
        }
    }

    // -------------------------------------------- //
    // BASIC: DAMAGE
    // -------------------------------------------- //

    public static void transferDamage( ItemStack stack,  JsonObject json, boolean stack2json) {
        // Durability is a weird name since it is the amount of damage.
        if (stack2json) {
            int damage = stack.getDurability();
            if (damage == DEFAULT_DAMAGE)
                return;
            json.addProperty(DAMAGE, damage);
        } else {
            JsonElement element = json.get(DAMAGE);
            if (element == null)
                return;
            stack.setDurability(element.getAsShort());
        }
    }

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    public static void transferMeta( ItemMeta meta,  JsonObject json, boolean meta2json) {
        transferUnspecificMeta(meta, json, meta2json);
        transferSpecificMeta(meta, json, meta2json);
    }

    // -------------------------------------------- //
    // UNSPECIFIC META
    // -------------------------------------------- //

    public static void transferUnspecificMeta( ItemMeta meta,  JsonObject json, boolean meta2json) {
        transferName(meta, json, meta2json);
        transferLore(meta, json, meta2json);
        transferEnchants(meta, json, meta2json);
        transferRepaircost(meta, json, meta2json);
    }

    // -------------------------------------------- //
    // UNSPECIFIC META: NAME
    // -------------------------------------------- //

    public static void transferName( ItemMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasDisplayName())
                return;
            json.addProperty(NAME, meta.getDisplayName());
        } else {
            JsonElement element = json.get(NAME);
            if (element == null)
                return;
            meta.setDisplayName(element.getAsString());
        }
    }

    // -------------------------------------------- //
    // UNSPECIFIC META: LORE
    // -------------------------------------------- //

    public static void transferLore( ItemMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasLore())
                return;
            json.add(LORE, convertStringList(meta.getLore()));
        } else {
            JsonElement element = json.get(LORE);
            if (element == null)
                return;
            meta.setLore(convertStringList(element));
        }
    }

    // -------------------------------------------- //
    // UNSPECIFIC META: ENCHANTS
    // -------------------------------------------- //

    public static void transferEnchants( ItemMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasEnchants())
                return;
            json.add(ENCHANTS, convertEnchantLevelMap(meta.getEnchants()));
        } else {
            JsonElement element = json.get(ENCHANTS);
            if (element == null)
                return;
            for (Entry<Enchantment, Integer> entry : convertEnchantLevelMap(element).entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
    }

    // -------------------------------------------- //
    // UNSPECIFIC META: REPAIRCOST
    // -------------------------------------------- //

    public static void transferRepaircost(ItemMeta meta,  JsonObject json, boolean meta2json) {
        if (!(meta instanceof Repairable))
            return;
        Repairable repairable = (Repairable) meta;

        if (meta2json) {
            if (!repairable.hasRepairCost())
                return;
            json.addProperty(REPAIRCOST, repairable.getRepairCost());
        } else {
            JsonElement element = json.get(REPAIRCOST);
            if (element == null)
                return;

            repairable.setRepairCost(element.getAsInt());
        }
    }

    // -------------------------------------------- //
    // SPECIFIC META
    // -------------------------------------------- //

    public static void transferSpecificMeta(ItemMeta meta,  JsonObject json, boolean meta2json) {
        if (meta instanceof BookMeta) {
            transferBookMeta((BookMeta) meta, json, meta2json);
        } else if (meta instanceof LeatherArmorMeta) {
            transferLeatherArmorMeta((LeatherArmorMeta) meta, json, meta2json);
        } else if (meta instanceof MapMeta) {
            transferMapMeta((MapMeta) meta, json, meta2json);
        } else if (meta instanceof PotionMeta) {
            transferPotionMeta((PotionMeta) meta, json, meta2json);
        } else if (meta instanceof SkullMeta) {
            transferSkullMeta((SkullMeta) meta, json, meta2json);
        } else if (meta instanceof EnchantmentStorageMeta) {
            transferEnchantmentStorageMeta((EnchantmentStorageMeta) meta, json, meta2json);
        }
    }

    // -------------------------------------------- //
    // SPECIFIC META: BOOK
    // -------------------------------------------- //

    public static void transferBookMeta( BookMeta meta,  JsonObject json, boolean meta2json) {
        transferTitle(meta, json, meta2json);
        transferAuthor(meta, json, meta2json);
        transferPages(meta, json, meta2json);
    }

    public static void transferTitle( BookMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasTitle())
                return;
            json.addProperty(BOOK_TITLE, meta.getTitle());
        } else {
            JsonElement element = json.get(BOOK_TITLE);
            if (element == null)
                return;
            meta.setTitle(element.getAsString());
        }
    }

    public static void transferAuthor( BookMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasTitle())
                return;
            json.addProperty(BOOK_AUTHOR, meta.getAuthor());
        } else {
            JsonElement element = json.get(BOOK_AUTHOR);
            if (element == null)
                return;
            meta.setAuthor(element.getAsString());
        }
    }

    public static void transferPages( BookMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasTitle())
                return;
            json.add(BOOK_PAGES, convertStringList(meta.getPages()));
        } else {
            JsonElement element = json.get(BOOK_PAGES);
            if (element == null)
                return;
            meta.setPages(convertStringList(element));
        }
    }

    // -------------------------------------------- //
    // SPECIFIC META: LEATHER ARMOR
    // -------------------------------------------- //

    public static void transferLeatherArmorMeta( LeatherArmorMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            Color color = meta.getColor();

            json.addProperty(LEATHER_ARMOR_COLOR, color.asRGB());
        } else {
            JsonElement element = json.get(LEATHER_ARMOR_COLOR);
            if (element == null)
                return;
            meta.setColor(Color.fromRGB(element.getAsInt()));
        }
    }

    // -------------------------------------------- //
    // SPECIFIC META: MAP
    // -------------------------------------------- //

    public static void transferMapMeta( MapMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.isScaling())
                return;
            json.addProperty(MAP_SCALING, true);
        } else {
            JsonElement element = json.get(MAP_SCALING);
            if (element == null)
                return;

            meta.setScaling(element.getAsBoolean());
        }
    }

    // -------------------------------------------- //
    // SPECIFIC META: POTION
    // -------------------------------------------- //

    public static void transferPotionMeta( PotionMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasCustomEffects())
                return;
            json.add(POTION_EFFECTS, convertPotionEffectList(meta.getCustomEffects()));
        } else {
            JsonElement element = json.get(POTION_EFFECTS);
            if (element == null)
                element = json.get(POTION_EFFECTS_OLD);
            if (element == null)
                return;

            meta.clearCustomEffects();
            for (PotionEffect pe : convertPotionEffectList(element)) {
                meta.addCustomEffect(pe, false);
            }
        }
    }

    // -------------------------------------------- //
    // SPECIFIC META: SKULL
    // -------------------------------------------- //

    public static void transferSkullMeta( SkullMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasOwner())
                return;
            json.addProperty(SKULL_OWNER, meta.getOwner());
        } else {
            JsonElement element = json.get(SKULL_OWNER);
            if (element == null)
                return;
            meta.setOwner(element.getAsString());
        }
    }

    public static void transferEnchantmentStorageMeta( EnchantmentStorageMeta meta,  JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasStoredEnchants())
                return;
            json.add(STORED_ENCHANTS, convertEnchantLevelMap(meta.getStoredEnchants()));
        } else {
            JsonElement element = json.get(STORED_ENCHANTS);
            if (element == null)
                return;
            for (Entry<Enchantment, Integer> entry : convertEnchantLevelMap(element).entrySet()) {
                meta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
    }

    // -------------------------------------------- //
    // MINI UTILS
    // -------------------------------------------- //

    // String List

    public static JsonArray convertStringList( Collection<String> strings) {
        JsonArray ret = new JsonArray();
        for (String string : strings) {
            ret.add(new JsonPrimitive(string));
        }
        return ret;
    }


    public static List<String> convertStringList( JsonElement jsonElement) {
        JsonArray array = jsonElement.getAsJsonArray();
        List<String> ret = new ArrayList<String>();

        Iterator<JsonElement> iter = array.iterator();
        while (iter.hasNext()) {
            JsonElement element = iter.next();
            ret.add(element.getAsString());
        }

        return ret;
    }

    // PotionEffect List

    public static JsonArray convertPotionEffectList( Collection<PotionEffect> potionEffects) {
        JsonArray ret = new JsonArray();
        for (PotionEffect e : potionEffects) {
            ret.add(PotionEffectAdapter.toJson(e));
        }
        return ret;
    }

    
    public static List<PotionEffect> convertPotionEffectList( JsonElement jsonElement) {
        if (jsonElement == null)
            return null;
        if (!jsonElement.isJsonArray())
            return null;
        JsonArray array = jsonElement.getAsJsonArray();

        List<PotionEffect> ret = new ArrayList<PotionEffect>();

        Iterator<JsonElement> iter = array.iterator();
        while (iter.hasNext()) {
            PotionEffect e = PotionEffectAdapter.fromJson(iter.next());
            if (e == null)
                continue;
            ret.add(e);
        }

        return ret;
    }

    // EnchantLevelMap

    public static JsonObject convertEnchantLevelMap( Map<Enchantment, Integer> enchantLevelMap) {

        final JsonObject toReturn = new JsonObject();

        enchantLevelMap.forEach((key,value) -> toReturn.addProperty(""+key.getId(),value));

        return toReturn;
    }


    public static Map<Enchantment, Integer> convertEnchantLevelMap( JsonElement jsonElement) {

        final JsonObject json = jsonElement.getAsJsonObject();

        final Map<Enchantment,Integer> toReturn = new HashMap<>();

        json.entrySet().forEach(entry -> toReturn.put(Enchantment.getById(Integer.valueOf(entry.getKey())),entry.getValue().getAsInt()));

        return toReturn;
    }

}
