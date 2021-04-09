package com.twitter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TwitterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TwitterApplicationTests {

    @Test
    public void main() {
        TwitterApplication.main(new String[] {});
    }
}
