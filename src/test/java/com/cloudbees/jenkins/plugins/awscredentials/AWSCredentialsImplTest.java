/*
 * The MIT License
 *
 *  Copyright (c) 2017, CloudBees, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 */

package com.cloudbees.jenkins.plugins.awscredentials;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.ItemGroup;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

public class AWSCredentialsImplTest {

    @Rule
    public final JenkinsRule rule = new JenkinsRule();

    @Ignore("Only used to create new test data")
    @Test
    public void serializeCredential() throws Exception {
        final AmazonWebServicesCredentials credential = new AWSCredentialsImpl(CredentialsScope.GLOBAL, null,
                "accessKey", "secretKey", "description");
        final ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("credential.ser"));
        try {
            os.writeObject(credential);
        } finally {
            os.close();
        }
    }

    /** Deserializes a credential. */
    private AmazonWebServicesCredentials deserializeCredential(@NonNull String name) throws Exception {
        return (AmazonWebServicesCredentials)new ObjectInputStream(getClass().getResourceAsStream(name)).readObject();
    }

    /** Deserializes a credential stored in the 1.16 format. */
    @Test
    public void deserializeCredential116() throws Exception {
        deserializeCredential("credential-1.16.ser");
    }

    /** Deserializes a credential stored in the 1.20 format with none of the new fields. */
    @Test
    public void deserializeCredential120NoNewFields() throws Exception {
        deserializeCredential("credential-1.20-no-new-fields.ser");
    }

    /** Deserializes a credential stored in the 1.20 format with the new fields. */
    @Test
    public void deserializeCredential120NewFields() throws Exception {
        deserializeCredential("credential-1.20-new-fields.ser");
    }

    /** Uses a credential stored in the 1.16 format. */
    @Test
    @LocalData
    public void useCredential116() throws Exception {
        List<AmazonWebServicesCredentials> list = CredentialsProvider.lookupCredentials(AmazonWebServicesCredentials.class, (ItemGroup) null, null, Collections.<DomainRequirement>emptyList());
        Assert.assertEquals("Credential found", 1, list.size());
    }

}
