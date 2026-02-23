CREATE TABLE app_user (
    id UUID,
    email VARCHAR(254) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT unique_email
        UNIQUE (email)
);

CREATE TABLE profile (
    user_id UUID,
    username VARCHAR(25) NOT NULL,
    bio VARCHAR(100),
    image VARCHAR(255),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (user_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE,
    CONSTRAINT unique_username
        UNIQUE (username)
);

CREATE TABLE follow (
    following_id UUID NOT NULL,
    follower_id UUID NOT NULL,

    PRIMARY KEY (following_id, follower_id),
    CONSTRAINT fk_follower
        FOREIGN KEY (following_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_following
        FOREIGN KEY (follower_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE,
    CONSTRAINT self_follow_not_allowed
        CHECK (following_id <> follower_id)
);

CREATE TABLE article (
    id UUID,
    slug VARCHAR(255) NOT NULL,
    title VARCHAR(100) NOT NULL,
    user_id UUID NOT NULL,
    description VARCHAR(100) NOT NULL,
    body TEXT NOT NULL,
    likes_count BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE,
    CONSTRAINT unique_article_slug
        UNIQUE (slug),
    CONSTRAINT likes_count_not_zero
        CHECK (likes_count >= 0)
);

CREATE TABLE favorite (
    article_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (user_id, article_id),
    CONSTRAINT fk_article
        FOREIGN KEY (article_id)
        REFERENCES article(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE
);

CREATE TABLE comment (
    id UUID,
    user_id UUID NOT NULL,
    article_id UUID NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE ,
    CONSTRAINT fk_article
        FOREIGN KEY (article_id)
        REFERENCES article(id)
        ON DELETE CASCADE
);

CREATE TABLE tag (
    id UUID,
    name VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT unique_tag_name
        UNIQUE (name)
);

CREATE TABLE taggings (
    article_id UUID NOT NULL,
    tag_id UUID NOT NULL,

    PRIMARY KEY (article_id, tag_id),
    CONSTRAINT fk_article
        FOREIGN KEY (article_id)
        REFERENCES article(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_tag
        FOREIGN KEY (tag_id)
        REFERENCES tag(id)
        ON DELETE CASCADE
);





