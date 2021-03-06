package com.jhostinlh.tiempokotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import com.microsoft.appcenter.distribute.Distribute
import com.microsoft.appcenter.distribute.DistributeListener
import com.microsoft.appcenter.distribute.ReleaseDetails
import com.microsoft.appcenter.distribute.UpdateAction


class MyDistributeListener : DistributeListener {
    override fun onReleaseAvailable(activity: Activity, releaseDetails: ReleaseDetails): Boolean {

        // Look at releaseDetails public methods to get version information, release notes text or release notes URL
        val versionName = releaseDetails.shortVersion
        val versionCode = releaseDetails.version
        val releaseNotes = releaseDetails.releaseNotes
        val releaseNotesUrl = releaseDetails.releaseNotesUrl

        // Build our own dialog title and message
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle("Version $versionName available!") // you should use a string resource instead, this is just a simple example
        dialogBuilder.setMessage(releaseNotes)
        Toast.makeText(activity,"entra en onReleaseAvailable",Toast.LENGTH_LONG).show()
        // Mimic default SDK buttons
        dialogBuilder.setPositiveButton(
            com.microsoft.appcenter.distribute.R.string.appcenter_distribute_update_dialog_download,
            object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Distribute.notifyUpdateAction(UpdateAction.UPDATE)
                }

            }
            )

        // We can postpone the release only if the update isn't mandatory
        if (!releaseDetails.isMandatoryUpdate) {
            dialogBuilder.setNegativeButton(
                com.microsoft.appcenter.distribute.R.string.appcenter_distribute_update_dialog_postpone,
                object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Distribute.notifyUpdateAction(UpdateAction.POSTPONE)
                    }

                }
            )
        }
        dialogBuilder.setCancelable(false) // if it's cancelable you should map cancel to postpone, but only for optional updates
        dialogBuilder.create().show()

        // Return true if you're using your own dialog, false otherwise
        return true
    }

    override fun onNoReleaseAvailable(activity: Activity) {
        Toast.makeText(
            activity,
            activity.getString(R.string.no_updates_available),
            Toast.LENGTH_LONG
        ).show()
    }
}