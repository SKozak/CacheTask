package pl.com.cupofcodes

import spock.lang.Specification

import java.time.Duration

class CacheFactorySpec extends Specification {

    def "Can create cache"() {
        given:
            CacheFactory cacheFactory = new CacheFactory()
        when:
            def cache = cacheFactory.createCache(200, Duration.ofMillis(100))
        then:
            cache != null
    }
}
