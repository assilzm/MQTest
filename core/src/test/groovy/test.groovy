/**
 *
 * @author: WeiWei
 * create: 19:41.
 * description:
 */
def a = [
        [1, 5, 3],
        [4, 2, 6],
        [7, 8, 9]
]


println a.find({
    it.contains(2)
})