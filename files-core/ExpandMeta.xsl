<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | * | text() | comment()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:if test="not(local-name() = 'base' or local-name() = 'xi')">
            <xsl:copy/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="comment()">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>
