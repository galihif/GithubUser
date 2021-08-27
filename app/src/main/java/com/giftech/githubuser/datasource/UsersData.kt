package com.giftech.githubuser.datasource

import com.giftech.githubuser.R
import com.giftech.githubuser.model.User

object UsersData {
    private val names = arrayOf(
        "Scarlett Johannsson",
        "Pandya Erlambang",
        "Galih Indra Firmansyah",
        "Benedict Cumberbatch",
        "Chris Evans",
        "Chris Hemsworth",
        "Tom Holland",
        "Chris Pratt",
        "Paul Rudd",
        "Robert Downey Junior",
    )

    private val usernames = arrayOf(
        "scarjo",
        "pandya",
        "galihif",
        "benedict",
        "evans",
        "hemsworth",
        "holland",
        "pratt",
        "rudd",
        "downey",
    )

    private val avatars = intArrayOf(
        R.drawable.scarjo,
        R.drawable.pandya,
        R.drawable.galih,
        R.drawable.benedict,
        R.drawable.evans,
        R.drawable.hemsworth,
        R.drawable.holland,
        R.drawable.pratt,
        R.drawable.rudd,
        R.drawable.downey,
        R.drawable.scarjo,
    )

    private val followings = intArrayOf(
        23,
        75,
        23,
        23,
        27,
        43,
        83,
        25,
        17,
        63,
    )
    private val followerss = intArrayOf(
        62,
        22,
        75,
        25,
        53,
        72,
        78,
        4522,
        456,
        342,
    )
    private val companys = arrayOf(
        "SHIELD",
        "UGM",
        "GIFTECH",
        "SANCTUM",
        "CAP",
        "LEVEU",
        "CONTY",
        "Stark",
        "ANTM",
        "Stark",
    )
    private val locations = arrayOf(
        "Wakanda",
        "Stark",
        "Cikande",
        "Jawa",
        "Jakarta",
        "Bandung",
        "Yogya",
        "Gotham",
        "Asgard",
        "Jawilan",
    )
    private val repositories = intArrayOf(
        23,
        23,
        23,
        23,
        12,
        11,
        53,
        23,
        66,
        23,
    )

    val listData : ArrayList<User>
        get() {
            val list = arrayListOf<User>()
//            for (position in names.indices){
//                val user = User()
//                user.name = names[position]
//                user.userName = usernames[position]
//                user.avatar = avatars[position]
//                user.following = followings[position]
//                user.followers = followerss[position]
//                user.company = companys[position]
//                user.location = locations[position]
//                user.repository = repositories[position]
//                list.add(user)
//            }
            return list
        }

}