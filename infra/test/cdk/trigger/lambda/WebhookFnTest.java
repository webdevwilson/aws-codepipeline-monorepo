package cdk.trigger.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHEventPayload;

public class WebhookFnTest {

    String payload = "{\n" +
            "    \"ref\": \"refs/heads/master\",\n" +
            "    \"before\": \"6962afc3296ef24ef457d72d70eeb967adb9ba79\",\n" +
            "    \"after\": \"01abe32d15e077c2ce6daf0ca77609c040337a21\",\n" +
            "    \"repository\": {\n" +
            "        \"id\": 399285444,\n" +
            "        \"node_id\": \"MDEwOlJlcG9zaXRvcnkzOTkyODU0NDQ=\",\n" +
            "        \"name\": \"aws-codepipeline-monorepo\",\n" +
            "        \"full_name\": \"webdevwilson/aws-codepipeline-monorepo\",\n" +
            "        \"private\": false,\n" +
            "        \"owner\": {\n" +
            "            \"name\": \"webdevwilson\",\n" +
            "            \"email\": \"kerry@allthingswilson.com\",\n" +
            "            \"login\": \"webdevwilson\",\n" +
            "            \"id\": 183272,\n" +
            "            \"node_id\": \"MDQ6VXNlcjE4MzI3Mg==\",\n" +
            "            \"avatar_url\": \"https://avatars.githubusercontent.com/u/183272?v=4\",\n" +
            "            \"gravatar_id\": \"\",\n" +
            "            \"url\": \"https://api.github.com/users/webdevwilson\",\n" +
            "            \"html_url\": \"https://github.com/webdevwilson\",\n" +
            "            \"followers_url\": \"https://api.github.com/users/webdevwilson/followers\",\n" +
            "            \"following_url\": \"https://api.github.com/users/webdevwilson/following{/other_user}\",\n" +
            "            \"gists_url\": \"https://api.github.com/users/webdevwilson/gists{/gist_id}\",\n" +
            "            \"starred_url\": \"https://api.github.com/users/webdevwilson/starred{/owner}{/repo}\",\n" +
            "            \"subscriptions_url\": \"https://api.github.com/users/webdevwilson/subscriptions\",\n" +
            "            \"organizations_url\": \"https://api.github.com/users/webdevwilson/orgs\",\n" +
            "            \"repos_url\": \"https://api.github.com/users/webdevwilson/repos\",\n" +
            "            \"events_url\": \"https://api.github.com/users/webdevwilson/events{/privacy}\",\n" +
            "            \"received_events_url\": \"https://api.github.com/users/webdevwilson/received_events\",\n" +
            "            \"type\": \"User\",\n" +
            "            \"site_admin\": false\n" +
            "        },\n" +
            "        \"html_url\": \"https://github.com/webdevwilson/aws-codepipeline-monorepo\",\n" +
            "        \"description\": null,\n" +
            "        \"fork\": false,\n" +
            "        \"url\": \"https://github.com/webdevwilson/aws-codepipeline-monorepo\",\n" +
            "        \"forks_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/forks\",\n" +
            "        \"keys_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/keys{/key_id}\",\n" +
            "        \"collaborators_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/collaborators{/collaborator}\",\n" +
            "        \"teams_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/teams\",\n" +
            "        \"hooks_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/hooks\",\n" +
            "        \"issue_events_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/issues/events{/number}\",\n" +
            "        \"events_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/events\",\n" +
            "        \"assignees_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/assignees{/user}\",\n" +
            "        \"branches_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/branches{/branch}\",\n" +
            "        \"tags_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/tags\",\n" +
            "        \"blobs_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/git/blobs{/sha}\",\n" +
            "        \"git_tags_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/git/tags{/sha}\",\n" +
            "        \"git_refs_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/git/refs{/sha}\",\n" +
            "        \"trees_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/git/trees{/sha}\",\n" +
            "        \"statuses_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/statuses/{sha}\",\n" +
            "        \"languages_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/languages\",\n" +
            "        \"stargazers_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/stargazers\",\n" +
            "        \"contributors_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/contributors\",\n" +
            "        \"subscribers_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/subscribers\",\n" +
            "        \"subscription_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/subscription\",\n" +
            "        \"commits_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/commits{/sha}\",\n" +
            "        \"git_commits_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/git/commits{/sha}\",\n" +
            "        \"comments_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/comments{/number}\",\n" +
            "        \"issue_comment_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/issues/comments{/number}\",\n" +
            "        \"contents_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/contents/{+path}\",\n" +
            "        \"compare_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/compare/{base}...{head}\",\n" +
            "        \"merges_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/merges\",\n" +
            "        \"archive_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/{archive_format}{/ref}\",\n" +
            "        \"downloads_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/downloads\",\n" +
            "        \"issues_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/issues{/number}\",\n" +
            "        \"pulls_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/pulls{/number}\",\n" +
            "        \"milestones_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/milestones{/number}\",\n" +
            "        \"notifications_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/notifications{?since,all,participating}\",\n" +
            "        \"labels_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/labels{/name}\",\n" +
            "        \"releases_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/releases{/id}\",\n" +
            "        \"deployments_url\": \"https://api.github.com/repos/webdevwilson/aws-codepipeline-monorepo/deployments\",\n" +
            "        \"created_at\": 1629764911,\n" +
            "        \"updated_at\": \"2021-08-24T01:05:48Z\",\n" +
            "        \"pushed_at\": 1629767416,\n" +
            "        \"git_url\": \"git://github.com/webdevwilson/aws-codepipeline-monorepo.git\",\n" +
            "        \"ssh_url\": \"git@github.com:webdevwilson/aws-codepipeline-monorepo.git\",\n" +
            "        \"clone_url\": \"https://github.com/webdevwilson/aws-codepipeline-monorepo.git\",\n" +
            "        \"svn_url\": \"https://github.com/webdevwilson/aws-codepipeline-monorepo\",\n" +
            "        \"homepage\": null,\n" +
            "        \"size\": 0,\n" +
            "        \"stargazers_count\": 0,\n" +
            "        \"watchers_count\": 0,\n" +
            "        \"language\": \"Java\",\n" +
            "        \"has_issues\": true,\n" +
            "        \"has_projects\": true,\n" +
            "        \"has_downloads\": true,\n" +
            "        \"has_wiki\": true,\n" +
            "        \"has_pages\": false,\n" +
            "        \"forks_count\": 0,\n" +
            "        \"mirror_url\": null,\n" +
            "        \"archived\": false,\n" +
            "        \"disabled\": false,\n" +
            "        \"open_issues_count\": 0,\n" +
            "        \"license\": null,\n" +
            "        \"forks\": 0,\n" +
            "        \"open_issues\": 0,\n" +
            "        \"watchers\": 0,\n" +
            "        \"default_branch\": \"master\",\n" +
            "        \"stargazers\": 0,\n" +
            "        \"master_branch\": \"master\"\n" +
            "    },\n" +
            "    \"pusher\": {\n" +
            "        \"name\": \"webdevwilson\",\n" +
            "        \"email\": \"kerry@allthingswilson.com\"\n" +
            "    },\n" +
            "    \"sender\": {\n" +
            "        \"login\": \"webdevwilson\",\n" +
            "        \"id\": 183272,\n" +
            "        \"node_id\": \"MDQ6VXNlcjE4MzI3Mg==\",\n" +
            "        \"avatar_url\": \"https://avatars.githubusercontent.com/u/183272?v=4\",\n" +
            "        \"gravatar_id\": \"\",\n" +
            "        \"url\": \"https://api.github.com/users/webdevwilson\",\n" +
            "        \"html_url\": \"https://github.com/webdevwilson\",\n" +
            "        \"followers_url\": \"https://api.github.com/users/webdevwilson/followers\",\n" +
            "        \"following_url\": \"https://api.github.com/users/webdevwilson/following{/other_user}\",\n" +
            "        \"gists_url\": \"https://api.github.com/users/webdevwilson/gists{/gist_id}\",\n" +
            "        \"starred_url\": \"https://api.github.com/users/webdevwilson/starred{/owner}{/repo}\",\n" +
            "        \"subscriptions_url\": \"https://api.github.com/users/webdevwilson/subscriptions\",\n" +
            "        \"organizations_url\": \"https://api.github.com/users/webdevwilson/orgs\",\n" +
            "        \"repos_url\": \"https://api.github.com/users/webdevwilson/repos\",\n" +
            "        \"events_url\": \"https://api.github.com/users/webdevwilson/events{/privacy}\",\n" +
            "        \"received_events_url\": \"https://api.github.com/users/webdevwilson/received_events\",\n" +
            "        \"type\": \"User\",\n" +
            "        \"site_admin\": false\n" +
            "    },\n" +
            "    \"created\": false,\n" +
            "    \"deleted\": false,\n" +
            "    \"forced\": false,\n" +
            "    \"base_ref\": null,\n" +
            "    \"compare\": \"https://github.com/webdevwilson/aws-codepipeline-monorepo/compare/6962afc3296e...01abe32d15e0\",\n" +
            "    \"commits\": [\n" +
            "        {\n" +
            "            \"id\": \"01abe32d15e077c2ce6daf0ca77609c040337a21\",\n" +
            "            \"tree_id\": \"a1b41ec8ee5b4bf02499237e6a44a4e5ed4fd5aa\",\n" +
            "            \"distinct\": true,\n" +
            "            \"message\": \"Output files\",\n" +
            "            \"timestamp\": \"2021-08-23T20:10:13-05:00\",\n" +
            "            \"url\": \"https://github.com/webdevwilson/aws-codepipeline-monorepo/commit/01abe32d15e077c2ce6daf0ca77609c040337a21\",\n" +
            "            \"author\": {\n" +
            "                \"name\": \"Kerry Wilson\",\n" +
            "                \"email\": \"kerry@allthingswilson.com\",\n" +
            "                \"username\": \"webdevwilson\"\n" +
            "            },\n" +
            "            \"committer\": {\n" +
            "                \"name\": \"Kerry Wilson\",\n" +
            "                \"email\": \"kerry@allthingswilson.com\",\n" +
            "                \"username\": \"webdevwilson\"\n" +
            "            },\n" +
            "            \"added\": [],\n" +
            "            \"removed\": [],\n" +
            "            \"modified\": [\n" +
            "                \"infra/src/cdk/lambda/WebhookFn.java\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"head_commit\": {\n" +
            "        \"id\": \"01abe32d15e077c2ce6daf0ca77609c040337a21\",\n" +
            "        \"tree_id\": \"a1b41ec8ee5b4bf02499237e6a44a4e5ed4fd5aa\",\n" +
            "        \"distinct\": true,\n" +
            "        \"message\": \"Output files\",\n" +
            "        \"timestamp\": \"2021-08-23T20:10:13-05:00\",\n" +
            "        \"url\": \"https://github.com/webdevwilson/aws-codepipeline-monorepo/commit/01abe32d15e077c2ce6daf0ca77609c040337a21\",\n" +
            "        \"author\": {\n" +
            "            \"name\": \"Kerry Wilson\",\n" +
            "            \"email\": \"kerry@allthingswilson.com\",\n" +
            "            \"username\": \"webdevwilson\"\n" +
            "        },\n" +
            "        \"committer\": {\n" +
            "            \"name\": \"Kerry Wilson\",\n" +
            "            \"email\": \"kerry@allthingswilson.com\",\n" +
            "            \"username\": \"webdevwilson\"\n" +
            "        },\n" +
            "        \"added\": [],\n" +
            "        \"removed\": [],\n" +
            "        \"modified\": [\n" +
            "            \"infra/src/cdk/lambda/WebhookFn.java\"\n" +
            "        ]\n" +
            "    }\n" +
            "}\n";

    @Test
    public void testIt() throws JsonProcessingException {
        GHEventPayload.Push push = new ObjectMapper().readValue(payload, GHEventPayload.Push.class);
        System.out.println("push: " + push.getCommits().get(0).getMessage());
    }
}