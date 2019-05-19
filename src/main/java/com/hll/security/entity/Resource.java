package com.hll.security.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Resource implements Serializable {
    private String id;
    private String url;
    private String resourceName;
    private String parentId;
    private int level;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "sys_uid")
    @GenericGenerator(name = "sys_uid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "resource_name")
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Basic
    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id) &&
                Objects.equals(url, resource.url) &&
                Objects.equals(resourceName, resource.resourceName) &&
                Objects.equals(parentId, resource.parentId) &&
                Objects.equals(level, resource.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, resourceName, parentId, level);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", parentId='" + parentId + '\'' +
                ", level=" + level +
                '}';
    }
}
