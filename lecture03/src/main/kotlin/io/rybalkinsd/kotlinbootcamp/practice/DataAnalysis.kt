package io.rybalkinsd.kotlinbootcamp.practice

class RawProfile(val rawData: String)

enum class DataSource {
    FACEBOOK,
    VK,
    LINKEDIN
}

sealed class Profile(
    var id: Long,
    var firstName: String? = null,
    var lastName: String? = null,
    var age: Int? = null,
    var dataSource: DataSource
)

/**
 * Task #1
 * Declare classes for all data sources
 */
class FacebookProfile(id: Long) : Profile(dataSource = DataSource.FACEBOOK, id = id)
class VkProfile(id: Long) : Profile(dataSource = DataSource.VK, id = id)
class LinkedinProfile(id: Long) : Profile(dataSource = DataSource.LINKEDIN, id = id)

/**
 * Here are Raw profiles to analyse
 */
val rawProfiles = listOf(
        RawProfile("""
            firstName=alice,
            age=16,
            source=facebook
            """.trimIndent()
        ),
        RawProfile("""
            firstName=Dent,
            lastName=kent,
            age=88,
            source=linkedin
            """.trimIndent()
        ),
        RawProfile("""
            firstName=alla,
            lastName=OloOlooLoasla,
            source=vk
            """.trimIndent()
        ),
        RawProfile("""
            firstName=bella,
            age=36,
            source=vk
            """.trimIndent()
        ),
        RawProfile("""
            firstName=angel,
            age=I will not tell you =),
            source=facebook
            """.trimIndent()
        ),

        RawProfile(
                """
            lastName=carol,
            source=vk
            age=49,
            """.trimIndent()
        ),
        RawProfile("""
            firstName=bob,
            lastName=John,
            age=47,
            source=linkedin
            """.trimIndent()
        ),
        RawProfile("""
            firstName=bob,
            lastName=John,
            age=47,
            source=linkedin
            """.trimIndent()
        ),
        RawProfile("""
            lastName=kent,
            firstName=dent,
            age=88,
            source=facebook
            """.trimIndent()
        )
)

fun Profile.isLike(p: Profile) = (this.firstName?.toLowerCase() == p.firstName?.toLowerCase() &&
        this.lastName?.toLowerCase() == p.lastName?.toLowerCase() &&
        this.age == p.age)

fun RawProfile.getParametr(parametrName: String) = this.rawData
        .substringAfter("$parametrName=", "")
        .substringBefore(',')
        .run { if (this.isBlank()) null else this }

fun List<RawProfile>.toListOfProfile() = this.mapIndexedNotNull { i, rp ->
    when (rp.getParametr("source")) {
        "facebook" -> FacebookProfile(i.toLong())
        "vk" -> VkProfile(i.toLong())
        "linkedin" -> LinkedinProfile(i.toLong())
        else -> null
    }.also {
        it?.age = rp.getParametr("age")?.toIntOrNull()
        it?.firstName = rp.getParametr("firstName")
        it?.lastName = rp.getParametr("lastName")
    }
}

fun List<Profile>.avgAge() =
        this.filter { it.age != null }
            .foldIndexed(0.0) { i, avg, p -> (avg * i + (p.age ?: 0)) / (i + 1) }

var profiles = rawProfiles.toListOfProfile()

/**
 * Task #2
 * Find the average age for each datasource (for profiles that has age)
 */
val avgAge: Map<DataSource, Double> = DataSource.values()
        .map { ds ->
            ds to profiles.filter { it.dataSource == ds }
                    .avgAge()
        }
        .toMap()

/**
 * Task #3
 * Group all user ids together with all profiles of this user.
 * We can assume users equality by : firstName & lastName & age
 */
val groupedProfiles: Map<Long, List<Profile>> = profiles.map { p ->
        p.id to profiles.filter { it.isLike(p) or it.equals(p) }
    }.toMap()