package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class FamScaleItem extends Item
{
    public FamScaleItem(Properties properties)
    {
        super(properties.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));
    }
}
