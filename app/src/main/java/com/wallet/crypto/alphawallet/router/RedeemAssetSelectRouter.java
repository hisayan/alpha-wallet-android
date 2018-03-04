package com.wallet.crypto.alphawallet.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.alphawallet.entity.Token;
import com.wallet.crypto.alphawallet.ui.RedeemAssetSelectActivity;

import static com.wallet.crypto.alphawallet.C.Key.TICKET;

/**
 * Created by James on 27/02/2018.
 */

public class RedeemAssetSelectRouter
{
    public void open(Context context, Token ticket) {
        Intent intent = new Intent(context, RedeemAssetSelectActivity.class);
        intent.putExtra(TICKET, ticket);
        context.startActivity(intent);
    }
}
